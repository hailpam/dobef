
package org.pm.datagrid.benchmark.stub;

import org.pm.datagrid.benchmark.Benchmark;
import org.pm.datagrid.benchmark.BenchmarkException;
import org.pm.datagrid.benchmark.Configuration;
import org.pm.datagrid.benchmark.Probe;

/**
 *
 * @author pmaresca
 */
public class DummyBenchmark  extends Benchmark
{

    public DummyBenchmark(Probe probe, Configuration conf) 
    {
        super(probe, conf);
    }

    @Override
    protected void specificConfigure(Configuration conf) 
    {
    }

    @Override
    protected void specificInit() throws BenchmarkException 
    {
    }

    @Override
    protected void specificWarmUp(int displacement) throws BenchmarkException 
    {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }
    }

    @Override
    protected void specificBenchmark(int displacement) throws BenchmarkException 
    {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
        }
    }

    @Override
    protected void specificWriteResults() throws BenchmarkException 
    {
    }
    
}
