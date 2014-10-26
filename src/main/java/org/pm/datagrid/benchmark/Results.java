/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pm.datagrid.benchmark;

/**
 *
 * @author pmaresca
 */
public class Results 
{
    private static final String RESULTS_LINE_TEMPLATE = "${SIDE},${DATA_SIZE},${RECORD}";
    
    private double[] timeRecords;
    private final String side;
    private final String dataSize;

    public Results(String side, String dataSize) 
    {
        this.side = side;
        this.dataSize = dataSize;
    }

    public double[] getTimeRecords() { return timeRecords; }

    public void setTimeRecords(double[] timeRecords) { this.timeRecords = timeRecords; }
    
    public String resultsToString() 
    {
        if(this.timeRecords == null || timeRecords.length == 0)
            throw new IllegalStateException("Time records not initialized correctly");
        
        StringBuilder sBuilder = new StringBuilder("");
        for(int i = 0; i < this.timeRecords.length; i++) {
            sBuilder.append(RESULTS_LINE_TEMPLATE.replace("${SIDE}", this.side)
                                                 .replace("${DATA_SIZE}", this.dataSize)
                                                 .replace("${RECORD}", 
                                                          Double.toString(this.timeRecords[i])));
            sBuilder.append("\n");
        }
        
        return sBuilder.toString();
    }
    
}
