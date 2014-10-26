
package org.pm.datagrid.benchmark;

/**
 * Benchmark settings placeholder.
 * 
 * @author pmaresca
 */
public class Configuration 
{
    private final int nrTests;
    private final int nrRepetitions;
    private final int nrWorkers;
    private final int nrWarmUps;
    private final int[] dataSizes;
    private final int nrWrites;
    private final int nrReads;
    
    private final String fileName;
    private final String filePath;
    private final String side;
    
    
    public Configuration(int nrTests, int nrRepetitions, int nrWorkers, int nrWarmUps, 
                         int[] dataSizes, int nrWrites, int nrReads, String fileOutput,
                         String filePath, String side) 
    {
        this.nrTests = nrTests;
        this.nrRepetitions = nrRepetitions;
        this.nrWorkers = nrWorkers;
        this.nrWarmUps = nrWarmUps;
        this.dataSizes = dataSizes;
        this.nrWrites = nrWrites;
        this.nrReads = nrReads;
        this.fileName = fileOutput;
        this.filePath = filePath;
        this.side = side;
    }

    public int getNrTests() { return this.nrTests; }

    public int getNrRepetitions() { return this.nrRepetitions; }

    public int getNrWorkers() { return this.nrWorkers; }
    
    public int getNrWarmUps() { return this.nrWarmUps; }

    public int[] getDataSizes() { return this.dataSizes; }

    public int getNrWrites() { return this.nrWrites; }

    public int getNrReads() { return this.nrReads; }

    public String getFileName() { return this.fileName; }
    
    public String getSide() { return this.side; }
    
    public String getFilePath() { return this.filePath; }
    
}
