
package org.pm.datagrid.benchmark;

import java.util.Random;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author pmaresca
 */
public class ResultsTest 
{
    
    private Results res;
    private Random rand;
    
    public ResultsTest() 
    {
    }
    
    @Before
    public void setUp() 
    {
        this.res = new Results("writer", "128");
        this.rand = new Random();
    }
    
    @After
    public void tearDown() 
    {
        this.res = null;
        this.rand = null;
    }
    
    @Test
    public void resultsToString() 
    {
        double[] samples = new double[100];
        for(int i = 0; i < samples.length; i++) {
            samples[i] = this.rand.nextGaussian();
        }
        this.res.setTimeRecords(samples);
        
        String output = this.res.resultsToString();
        assertNotNull(output);
        assertNotSame("", output);
    }
    
}
