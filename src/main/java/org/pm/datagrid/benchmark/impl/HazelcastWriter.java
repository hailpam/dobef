
package org.pm.datagrid.benchmark.impl;

import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author pmaresca
 */
public class HazelcastWriter implements Runnable
{

    private final int nrWrites;
    private final int dataSize;
    
    private String mapName;
    
    private final Random rand;
    
    private final ConcurrentMap<Integer, String> mapGrid;
    
    public HazelcastWriter(int nrWrites, int dataSize, String mapName,
                            ConcurrentMap<Integer, String> mapGrid) 
    {
        this.nrWrites = nrWrites;
        this.dataSize = dataSize;
        this.mapName = mapName;
        
        this.rand = new Random();
        
        this.mapGrid = mapGrid;
    }
    
    @Override
    public void run() 
    {
        for(int i = 0; i < this.nrWrites; i++) {
            Integer key = this.rand.nextInt(nrWrites);
            String value = RandomStringUtils.randomAlphabetic(this.dataSize);
            
            this.mapGrid.put(key, value);
        }
    }
    
}
