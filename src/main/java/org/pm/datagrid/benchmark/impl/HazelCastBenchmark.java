
package org.pm.datagrid.benchmark.impl;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.RandomStringUtils;
import org.pm.datagrid.benchmark.Benchmark;
import org.pm.datagrid.benchmark.BenchmarkException;
import org.pm.datagrid.benchmark.Configuration;
import org.pm.datagrid.benchmark.Probe;

/**
 *
 * @author pmaresca
 */
public class HazelCastBenchmark extends Benchmark
{

    private String mapName;
    private Random rand;
    private HazelcastInstance dataGrid;


    public HazelCastBenchmark(Probe probe, Configuration conf)
    {
        super(probe, conf);

        this.rand = new Random();
    }

    @Override
    protected void specificConfigure(Configuration conf)
    {
        this.mapName = "benchmark-map";
    }

    @Override
    protected void specificInit() throws BenchmarkException
    {
        // initializing Hazelcast
        Config conf = new Config(RandomStringUtils.random(4));
        this.dataGrid = Hazelcast.newHazelcastInstance(conf);
        ConcurrentMap<Integer, String> mapGrid = this.dataGrid.getMap(this.mapName);


        for(int s = 0; s < this.config.getNrWarmUps(); s++) {
            int idx = s*this.config.getNrWorkers();
            int i = 0;
            while(i < this.config.getNrWorkers()) {
                this.warmUpworkers[(i + idx)] = new Thread(new HazelcastWriter(
                                                        this.config.getNrWrites(),
                                                        this.rand.nextInt(4096),
                                                        this.mapName, mapGrid));
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
                this.workers[(i + idx)] = new Thread(new HazelcastWriter(
                                                        this.config.getNrWrites(),
                                                        this.config.getDataSizes()[s],
                                                        this.mapName, mapGrid));
                i++;
            }
        }

    }

    @Override
    protected void specificWarmUp(int displacement) throws BenchmarkException {

        try {
            ExecutorService es = Executors.newCachedThreadPool();
            List<Future<?>> futures = new ArrayList<>();
            for(int i = 0; i < this.config.getNrWorkers(); i++) {
                futures.add(es.submit(this.warmUpworkers[(i + displacement) % this.warmUpworkers.length]));
            }

            for (Future<?> future : futures) {
                future.get();
            }
            es.shutdown();
        } catch (Exception ie) {
            System.err.println("  Error [" + ie.getMessage() + "]");
        }
    }

    @Override
    protected void specificBenchmark(int displacement) throws BenchmarkException {
        try {
            ExecutorService es = Executors.newCachedThreadPool();
            List<Future<?>> futures = new ArrayList<>();
            for(int i = 0; i < this.config.getNrWorkers(); i++) {
                futures.add(es.submit(this.workers[(i + displacement)% this.workers.length]));
            }

            for (Future<?> future : futures) {
                future.get();
            }
            es.shutdown();
        } catch (Exception ie) {
            System.err.println("  Error [" + ie.getMessage() + "]");
        }
    }

    @Override
    protected void specificWriteResults() throws BenchmarkException
    {
    }

    @Override
    protected void specificTearDown() throws BenchmarkException
    {
        this.dataGrid.shutdown();
    }

}
