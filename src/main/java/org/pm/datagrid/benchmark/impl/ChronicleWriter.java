package org.pm.datagrid.benchmark.impl;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;
import java.util.concurrent.ConcurrentMap;

/**
 * @author pmaresca
 */
public class ChronicleWriter implements Runnable {

    private final int nrWrites;
    private final int dataSize;

    private String mapName;

    private final Random rand;

    private final ConcurrentMap<Integer, String> mapGrid;

    public ChronicleWriter(int nrWrites, int dataSize, String mapName,
                           ConcurrentMap<Integer, String> mapGrid) {
        this.nrWrites = nrWrites;
        this.dataSize = dataSize;
        this.mapName = mapName;

        this.rand = new Random();

        this.mapGrid = mapGrid;
    }

    @Override
    public void run() {
        // very expensive to re-create each time, changes results.
        String value = RandomStringUtils.randomAlphabetic(this.dataSize);
        for (int i = 0; i < this.nrWrites; i++) {
            Integer key = this.rand.nextInt(nrWrites);

            this.mapGrid.put(key, value);
        }
    }

}
