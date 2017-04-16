
package org.pm.datagrid.benchmark;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import org.apache.commons.io.FileUtils;

/**
 * Benchmark data type. It defines an abstract class able to implement the generic
 * phases of any generic benchmark.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public abstract class Benchmark extends Observable
{   
    private static final String SUMMARY_TEMPLATE = "\nBenchmark Summary- Data [${DATA}], "+
                                                        "Test(s) [${TESTS}], "+
                                                        "Repetition(s) [${REPETITIONS}]" 
                                                 +"\n{\n"
                                                 + "  elapsed[ms]        : ${ELAPSED}\n"
                                                 + "  mean[us]           : ${MEAN}\n"
                                                 + "  variance[us]       : ${VARIANCE}\n"
                                                 + "  std deviation[us]  : ${STDDEV}\n"
                                                 + "  min[us]            : ${MIN}\n"
                                                 + "  max[us]            : ${MAX}\n"
                                                 + "  95th percentile[us]: ${PERCENTILE}\n"
                                                 + "}\n";
    
    private static final String DATE_FORMAT = "yyyy_MM_dd_HH_mm_ss_";
    
    private boolean configured;
    private boolean initialized;
    private boolean warmedUp;
    private boolean benchmarked;
    private boolean resWritten;
    
    private Map<Integer, Results> results;
    
    private long elapsed;
    
    private final Probe probe;
    
    protected final Configuration config;
    
    private final SimpleDateFormat formatter;
    
    protected Runnable[] workers;
    protected Runnable[] warmUpworkers;
    
    
    /**
     * 
     * @param probe
     * @param conf 
     */
    protected Benchmark(Probe probe, Configuration conf) 
    {
        this.configured = false;
        this.initialized = false;
        this.warmedUp = false;
        this.benchmarked = false;
        this.resWritten = false;
        this.probe = probe;
        this.elapsed = 0L;
        this.config = conf;
        this.formatter = new SimpleDateFormat(DATE_FORMAT);
    }
    
    /**
     * 
     */
    public final void configure() 
    {
        if(!this.configured) {
            System.out.println(":configure");
            Preconditions.checkNotNull(this.config);
            this.configured = true;
            
            this.specificConfigure(this.config);
            System.out.println(" DONE");
        }
    }
    
    /**
     * 
     * @throws BenchmarkException 
     */
    @SuppressWarnings("UseOfObsoleteCollectionType")
    public final void init() throws BenchmarkException 
    {
        if(!this.configured)
            throw new BenchmarkException("Benchmark must be configured");
        
        if(!this.initialized) {
            System.out.println(":init");
            this.initialized = true;
            
            this.workers = new Runnable[this.config.getNrWorkers()*
                                            this.config.getDataSizes().length*
                                            this.config.getNrTests()*
                                            this.config.getNrRepetitions()];
            this.warmUpworkers = new Runnable[this.config.getNrWorkers()*
                                            this.config.getNrWarmUps()];
            this.results = new Hashtable<>(this.config.getDataSizes().length);
            for(int i = 0; i < this.config.getDataSizes().length; i++) {
                Results res = new Results(Integer.toString(this.config.getDataSizes()[i]), 
                                          this.config.getSide());
                this.results.put(this.config.getDataSizes()[i], res);
            }
            
            this.specificInit();
            System.out.println(" DONE");
        }
    }
    
    /**
     * 
     * @throws BenchmarkException 
     */
    public final void warmUp() throws BenchmarkException 
    {
        if(!this.initialized)
            throw new BenchmarkException("Benchmark must be initialized");
        
        if(!this.warmedUp) {
            System.out.println(":warmUp");
            this.warmedUp = true;
            long startWarmUp = System.currentTimeMillis();
            for(int i = 0; i < this.config.getNrWarmUps(); i++) {
                int idx = i*this.config.getNrWorkers();
                this.specificWarmUp(idx);
            }
            System.out.println(" DONE - " +(System.currentTimeMillis() - startWarmUp)
                                +"ms");
        }
    }
    
    /**
     * 
     * @throws BenchmarkException 
     */
    public final void benchmark() throws BenchmarkException 
    {
        if(!this.warmedUp)
            throw new BenchmarkException("Benchmark must be warmed up");
        
        if(!this.benchmarked) {
            System.out.println(":benchmark");
            this.benchmarked = true;
            
            long benchStart = System.currentTimeMillis();
            int displacement = 0;
            for(int d = 0; d < this.config.getDataSizes().length; d++) {
                for(int i = 0; i < this.config.getNrTests(); i++) {
                    long testStart = System.nanoTime();
                    for(int j = 0; j < this.config.getNrRepetitions(); j++) {
//                        TODO : fix index based approach
//                        int displacement = d*(this.config.getNrTests()*
//                                                this.config.getNrRepetitions()*
//                                                this.config.getNrWorkers())
//                                           +i*(this.config.getNrTests()*
//                                                this.config.getNrRepetitions()) 
//                                           +j*this.config.getNrWorkers();
                        this.specificBenchmark(displacement);
                        displacement += this.config.getNrWorkers();
                    }
                    // notify probe with measure
                    Long testEnd = System.nanoTime() - testStart;
                    this.probe.update((testEnd.doubleValue() / this.config.getNrRepetitions()));
                }
                // print out summary
                this.elapsed = System.currentTimeMillis() - benchStart;
                String summary = this.printBenchmarkSummary(Integer.toString(
                                                                this.config.getDataSizes()[d]));
                System.out.println(summary);
                // notifying any listener
                this.notifyObservers(summary);
                this.setChanged();
                // probe reset
                this.results.get(this.config.getDataSizes()[d])
                                .setTimeRecords(this.probe.getTimeRecords());
                this.probe.reset();
            }
            
            
        }
    }
    
    /**
     * 
     * @throws BenchmarkException 
     */
    public final void writeResults() throws BenchmarkException 
    {
        if(!this.benchmarked)
            throw new BenchmarkException("Benchmark must run");
        
        if(!this.resWritten) {
            System.out.println(":writeResults");
            this.resWritten = true;
            
            this.specificWriteResults();
            List<String> lines = new LinkedList<>();
            for(Integer key: this.results.keySet())
                lines.add(this.results.get(key).resultsToString());
            try {
                File resFile = new File(this.config.getFilePath() +File.separator
                                        +this.formatter.format(new Date())
                                        +this.config.getFileName());
                FileUtils.writeLines(resFile, lines);
            } catch (IOException ex) {
                System.err.println(" ERROR - ["+ex.toString()+"]");
                throw new BenchmarkException(ex.getMessage());
            }
            
            System.out.println(" DONE");
        }
    }
    
    /**
     * 
     * @throws BenchmarkException 
     */
    public final void tearDown() throws BenchmarkException {}
    
    /**
     * 
     * @param conf 
     */
    protected abstract void specificConfigure(Configuration conf);
    
    /**
     * 
     * @throws BenchmarkException 
     */
    protected abstract void specificInit() throws BenchmarkException ;
    
    /**
     * 
     * @param displacement
     * @throws BenchmarkException 
     */
    protected abstract void specificWarmUp(int displacement) 
                                                throws BenchmarkException ;
    
    /**
     * 
     * @param displacement
     * @throws BenchmarkException 
     */
    protected abstract void specificBenchmark(int displacement) 
                                                throws BenchmarkException ;

    /**
     * 
     * @throws BenchmarkException 
     */
    protected abstract void specificWriteResults() throws BenchmarkException ;
    
    /**
     * 
     * @throws BenchmarkException 
     */
    protected abstract void specificTearDown() throws BenchmarkException;
    
    /**
     * 
     * @param dataSize
     * @return 
     */
    private String printBenchmarkSummary(String dataSize) 
    {
        return SUMMARY_TEMPLATE
                .replace("${DATA}", dataSize)
                .replace("${ELAPSED}", String.format("%d", (long) this.elapsed))
                .replace("${MEAN}", String.format("%.1f", this.probe.mean()))
                .replace("${VARIANCE}", String.format("%d", (long) this.probe.variance()))
                .replace("${MAX}", String.format("%.1f", this.probe.max()))
                .replace("${MIN}", String.format("%.1f", this.probe.min()))
                .replace("${PERCENTILE}", String.format("%.1f", this.probe.percentile(0.95)));
    }
    
}
