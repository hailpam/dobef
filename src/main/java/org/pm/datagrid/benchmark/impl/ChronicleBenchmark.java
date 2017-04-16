package org.pm.datagrid.benchmark.impl;

import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;
import org.pm.datagrid.benchmark.Benchmark;
import org.pm.datagrid.benchmark.BenchmarkException;
import org.pm.datagrid.benchmark.Configuration;
import org.pm.datagrid.benchmark.Probe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author pmaresca
 */
public class ChronicleBenchmark extends Benchmark {

    private String mapName;
    private Random rand;
    private ChronicleMap<Integer, String> map;

    public ChronicleBenchmark(Probe probe, Configuration conf) {
        super(probe, conf);

        this.rand = new Random();
    }

    @Override
    protected void specificConfigure(Configuration conf) {
        this.mapName = "benchmark-map";
    }

    @Override
    protected void specificInit() throws BenchmarkException {
        // initializing Chornicle
        map = getMap(this.mapName, Integer.class, String.class, 128);
        ConcurrentMap<Integer, String> mapGrid = map;


        for (int s = 0; s < this.config.getNrWarmUps(); s++) {
            int idx = s * this.config.getNrWorkers();
            int i = 0;
            while (i < this.config.getNrWorkers()) {
                this.warmUpworkers[(i + idx)] = new ChronicleWriter(
                        this.config.getNrWrites(),
                        this.rand.nextInt(4096),
                        this.mapName, mapGrid);
                i++;
            }
        }

        for (int s = 0; s < this.config.getDataSizes().length; s++) {
            int idx = s * this.config.getNrTests() *
                    this.config.getNrRepetitions() *
                    this.config.getNrWorkers();
            int i = 0;
            while (i < this.config.getNrTests() *
                    this.config.getNrRepetitions() * this.config.getNrWorkers()) {
                this.workers[(i + idx)] = new ChronicleWriter(
                        this.config.getNrWrites(),
                        this.config.getDataSizes()[s],
                        this.mapName, mapGrid);
                i++;
            }
        }

    }

    private ChronicleMap<Integer, String> getMap(String mapName, Class<Integer> kClass, Class<String> vClass, int entrySize) throws BenchmarkException {
        try {
            return ChronicleMapBuilder
                    .of(kClass, vClass)
                    .entrySize(entrySize)
                    .createPersistedTo(new File(mapName));
        } catch (IOException e) {
            throw new BenchmarkException("", e);
        }
    }

    @Override
    protected void specificWarmUp(int displacement) throws BenchmarkException {
        try {
            ExecutorService es = Executors.newCachedThreadPool();
            List<Future<?>> futures = new ArrayList<>();
            for (int i = 0; i < this.config.getNrWorkers(); i++) {
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
    protected void specificBenchmark(int displacement)
            throws BenchmarkException {
        try {
            ExecutorService es = Executors.newCachedThreadPool();
            List<Future<?>> futures = new ArrayList<>();
            for (int i = 0; i < this.config.getNrWorkers(); i++) {
                futures.add(es.submit(this.workers[(i + displacement) % this.workers.length]));
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
    protected void specificWriteResults() throws BenchmarkException {
    }

    @Override
    protected void specificTearDown() throws BenchmarkException {
        try {
            this.map.close();
        } catch (IOException e) {
            throw new BenchmarkException("", e);
        }
    }

}
