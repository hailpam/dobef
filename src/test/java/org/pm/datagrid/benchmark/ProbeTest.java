
package org.pm.datagrid.benchmark;

import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pmaresca
 */
public class ProbeTest 
{
    private static int NR_SAMPLES = 100;
    private static int RESCALE = 1;
    
    private Probe p;
    
    
    @Before
    public void setUp() 
    {
        this.p = new Probe(NR_SAMPLES, RESCALE);
    }
    
    @After
    public void tearDown() 
    {
        this.p = null;
    }
    
    @Test
    public void update() 
    {
        Random rand = new Random();
        for(int i = 0; i < NR_SAMPLES; i++) {
            double d = rand.nextGaussian();
            if(d < 0.0)
                d = Math.abs(d);
            this.p.update(d);
        }
        assertTrue(this.p.mean() > 0.0);
        double mean = this.p.mean();
        assertTrue(this.p.min() >  0.0);
        assertTrue(this.p.max() > 0.0);
        assertTrue(this.p.percentile(0.95) > 0.0);
        assertTrue(this.p.max() > this.p.min());
        assertTrue(this.p.max() > this.p.percentile(0.95));
        
        this.p.reset();
        for(int i = 0; i < NR_SAMPLES; i++) {
            double d = rand.nextGaussian();
            if(d < 0.0)
                d = Math.abs(d);
            this.p.update(d);
        }
        assertTrue(mean != this.p.mean());
        assertTrue(this.p.mean() > 0.0);
        assertTrue(this.p.min() >  0.0);
        assertTrue(this.p.max() > 0.0);
        assertTrue(this.p.percentile(0.95) > 0.0);
        assertTrue(this.p.max() > this.p.min());
        assertTrue(this.p.max() > this.p.percentile(0.95));
        this.p.reset();
    }
    
}
