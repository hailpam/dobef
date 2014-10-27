
package org.pm.datagrid.benchmark.impl;

import java.util.Random;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author pmaresca
 */
public class HazelcastReader implements Runnable
{

    private final int nrReads;
    private final int dataSize;
    
    private String mapName;
    
    private final Random rand;
    
    private final ConcurrentMap<Integer, String> mapGrid;
    
    public HazelcastReader(int nrReads, int dataSize, String mapName,
                            ConcurrentMap<Integer, String> mapGrid) 
    {
        this.nrReads = nrReads;
        this.dataSize = dataSize;
        this.mapName = mapName;
        
        this.rand = new Random();
        
        this.mapGrid = mapGrid;
    }
    
    @Override
    public void run() 
    {
        for(int i = 0; i < this.nrReads; i++) {
            Integer key = this.rand.nextInt(nrReads);
            
            this.mapGrid.get(key);
        }
    }
    
}
