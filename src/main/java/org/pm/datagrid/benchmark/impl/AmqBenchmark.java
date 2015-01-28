
package org.pm.datagrid.benchmark.impl;

import java.util.Random;
import org.pm.datagrid.benchmark.Benchmark;
import org.pm.datagrid.benchmark.BenchmarkException;
import org.pm.datagrid.benchmark.Configuration;
import org.pm.datagrid.benchmark.Probe;

/**
 *
 * @author pmaresca
 * @since
 * @version
 */
public class AmqBenchmark extends Benchmark
{
    private Random rand;
    
    private String queue;

    public AmqBenchmark(Probe probe, Configuration conf) 
    {
        super(probe, conf);
        
        this.rand = new Random();
    }
    
    @Override
    protected void specificConfigure(Configuration conf) 
    {
        this.queue = "test.amq.queue";
    }

    @Override
    protected void specificInit() throws BenchmarkException 
    {
        for(int s = 0; s < this.config.getNrWarmUps(); s++) {
            int idx = s*this.config.getNrWorkers();
            int i = 0;
            while(i < this.config.getNrWorkers()) {
                this.warmUpworkers[(i + idx)] = new Thread(new AmqProducer(
                                                        this.config.getNrWrites(), 
                                                        this.rand.nextInt(4096), 
                                                        this.queue));
                i++;
            }
        }
        
        for(int s = 0; s < this.config.getDataSizes().length; s++) {
            int idx = s*this.config.getNrTests()*
                                this.config.getNrRepetitions()*
                                this.config.getNrWorkers();
            int i = 0;
            while(i < this.config.getNrTests()*
                        this.config.getNrRepetitions()*this.config.getNrWorkers()) {
                this.workers[(i + idx)] = new Thread(new AmqProducer(
                                                        this.config.getNrWrites(), 
                                                        this.config.getDataSizes()[s], 
                                                        this.queue));
                i++;
            }
        }
    }

    @Override
    protected void specificWarmUp(int displacement) throws BenchmarkException 
    {
        try {
            int i = 0;
            while(i < this.config.getNrWorkers()) {
                this.warmUpworkers[(i + displacement)].start();
                i++;
            }
            
            i = 0;
            while(i < this.config.getNrWorkers()) {
                this.warmUpworkers[(i + displacement)].join();
                i++;
            }
        }catch(InterruptedException ie) { 
            System.err.println("  Error ["+ie.getMessage()+"]");
        }
    }

    @Override
    protected void specificBenchmark(int displacement) throws BenchmarkException 
    {
        try {
            int i = 0;
            while(i < this.config.getNrWorkers()) {
                this.workers[(i + displacement)].start();
                i++;
            }
            
            i = 0;
            while(i < this.config.getNrWorkers()) {
                this.workers[(i + displacement)].join();
                i++;
            }
        }catch(InterruptedException ie) { 
            System.err.println("  Error ["+ie.getMessage()+"]");
        }
    }

    @Override
    protected void specificWriteResults() throws BenchmarkException 
    {
    }

    @Override
    protected void specificTearDown() throws BenchmarkException 
    {
    }
    
}
