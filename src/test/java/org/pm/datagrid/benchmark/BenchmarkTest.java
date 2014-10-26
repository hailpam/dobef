
package org.pm.datagrid.benchmark;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.pm.datagrid.benchmark.stub.DummyBenchmark;

/**
 *
 * @author pmaresca
 */
public class BenchmarkTest 
{
    
    private Benchmark b;
    
    
    public BenchmarkTest() 
    {
        int[] dataSize = new int[3];
        dataSize[0] = 64;
        dataSize[1] = 128;
        dataSize[2] = 256;
        Probe p = new Probe(10, 1);
        Configuration c = null;
        try {
            c = new ConfigurationBuilder().nrTests(10)
                                          .nrRepetitions(3)
                                          .nrWarmUps(1)
                                          .nrWorkers(1)
                                          .nrWrites(10)
                                          .nrReads(10)
                                          .dataSizes(dataSize)
                                          .side("writer")
                                          .fileName("dummy-benchmark")
                                          .filePath(System.getProperty("user.home"))
                                          .build();
        } catch (BenchmarkException ex) {
            fail("Unable to initialise");
        }
        b = new DummyBenchmark(p, c);
    }
    
    @Before
    public void setUp() 
    {
    }
    
    @After
    public void tearDown() 
    {
    }

    @Test
    public void benchmark() 
    {
        try {
            this.b.benchmark();
            this.b.writeResults();
            this.b.init();
        } catch (BenchmarkException ex) {
            assertTrue(true);
        }
        
        
        try {
            this.b.configure();
            this.b.init();
            this.b.warmUp();
            this.b.benchmark();
            this.b.writeResults();
        } catch (BenchmarkException ex) {
            ex.printStackTrace();
            fail("Excpected a good run");
        }
    }
    
}
