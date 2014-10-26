
package org.pm.datagrid.benchmark;


import org.apache.commons.math3.stat.StatUtils;

/**
 *
 * @author pmaresca
 */
public class Probe
{
    private final int nrSamples;
    private int nextSample;
    // from nanoseconds to 
    private final int rescale;
    private double[] timeRecords;
    private Double min;
    private Double max;
    private double mean;
    private double variance;
    
    
    public Probe(int samples, int rescale) 
    {
        this.nrSamples = samples;
        this.nextSample = 0;
        this.rescale = rescale;
        this.timeRecords = new double[samples];
        this.max = Double.MIN_VALUE;
        this.min = Double.MAX_VALUE;
        this.mean = 0.0;
        this.variance = 0.0;
    }

    Probe() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public void update(Double record) 
    {
        if(this.nextSample > this.nrSamples - 1)
            throw new IllegalStateException("No more Samples can be accepted");
        synchronized(this) {
            record = record/this.rescale;
            this.timeRecords[this.nextSample++] = record;
            if(record > max)
                max = record;
            if(record < min)
                min = record;
        }
    }
    
    public synchronized void reset() 
    {
        if(this.nextSample != this.nrSamples)
            throw new IllegalStateException("On-going benchmark: reset not allowed");
        this.timeRecords = new double[this.nrSamples];
        this.max = Double.MIN_VALUE;
        this.min = Double.MAX_VALUE;
        this.mean = 0.0;
        this.variance = 0.0;
        this.nextSample = 0;
    }
    
    public synchronized double mean() 
    {
        if(this.mean == 0.0 && (this.nextSample == this.nextSample - 1))
            return StatUtils.mean(this.timeRecords);
        else 
            return this.max;
    }
    
    public synchronized double variance() 
    {
        if(this.variance == 0.0 && (this.nextSample == this.nextSample))
            return StatUtils.variance(this.timeRecords);
        else 
            return this.variance;
    }
    
    public synchronized double percentile(double p) 
    { return StatUtils.percentile(this.timeRecords, p) ; 
    }
    
    public synchronized double min() { return this.min; }
    
    public synchronized double  max() { return this.max; }
    
    public double[] getTimeRecords() { return this.timeRecords; }
    
}
