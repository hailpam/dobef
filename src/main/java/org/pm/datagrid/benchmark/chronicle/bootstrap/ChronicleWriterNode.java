package org.pm.datagrid.benchmark.chronicle.bootstrap;

import org.pm.datagrid.benchmark.*;
import org.pm.datagrid.benchmark.chronicle.impl.ChronicleBenchmark;
import org.pm.datagrid.benchmark.impl.HazelCastBenchmark;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author pmaresca
 */
public class ChronicleWriterNode {
    private static final String FILE_PROPERTIES = "benchmark.properties";


    public static void main(String[] args) {
        Benchmark bench = null;
        try {
            InputStream is = ChronicleWriterNode.class.getClassLoader().getResourceAsStream(FILE_PROPERTIES);
            Map<String, Configuration> confs = ConfigurationReader.readConfiguration(is);
            Configuration conf = confs.get("writer");
            Probe probe = new Probe(conf.getNrTests(), 1000);
            bench = new ChronicleBenchmark(probe, conf);
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


    private static void logError(Exception e) {
        e.printStackTrace();
        System.err.println("Error: [" + e.getMessage() + "]");

        System.exit(1);
    }

}
