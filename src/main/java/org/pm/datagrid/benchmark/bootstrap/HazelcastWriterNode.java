
package org.pm.datagrid.benchmark.bootstrap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.pm.datagrid.benchmark.Benchmark;

import org.pm.datagrid.benchmark.BenchmarkException;
import org.pm.datagrid.benchmark.Configuration;
import org.pm.datagrid.benchmark.ConfigurationReader;
import org.pm.datagrid.benchmark.Probe;
import org.pm.datagrid.benchmark.impl.HazelCastBenchmark;

/**
 *
 * @author pmaresca
 */
public class HazelcastWriterNode 
{
    private static final String FILE_PROPERTIES = "/home/pmaresca/Developments/"
            + "workspaces/datagrid-benchmark/src/main/resources/benchmark.properties";
    
    
    public static void main(String[] args) 
    {
        Benchmark bench = null;
        try {
            InputStream is = new FileInputStream(FILE_PROPERTIES);
            Map<String, Configuration> confs = ConfigurationReader.readConfiguration(is);
            Configuration conf = confs.get("writer");
            Probe probe = new Probe(conf.getNrTests(), 1000);
            bench = new HazelCastBenchmark(probe, conf);
            bench.configure();
            bench.init();
            bench.warmUp();
            bench.benchmark();
            bench.writeResults();
            bench.tearDown();
        } catch (IOException ex) {
            logError(ex);
        } catch (IllegalStateException ex) {
            logError(ex);
        } catch (BenchmarkException ex) {
            logError(ex);
        }
        
        System.exit(0);
    }
    
    
    private static void logError(Exception e) 
    {
        e.printStackTrace();
        System.err.println("Error: ["+e.getMessage()+"]");
        
        System.exit(1);
    }
    
}
