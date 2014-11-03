
package org.pm.datagrid.benchmark;

/**
 * Benchmark settings placeholder.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
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
    
    
    /**
     * 
     * @param nrTests
     * @param nrRepetitions
     * @param nrWorkers
     * @param nrWarmUps
     * @param dataSizes
     * @param nrWrites
     * @param nrReads
     * @param fileOutput
     * @param filePath
     * @param side 
     */
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

    /**
     * 
     * @return 
     */
    public int getNrTests() { return this.nrTests; }

    /**
     * 
     * @return 
     */
    public int getNrRepetitions() { return this.nrRepetitions; }

    /**
     * 
     * @return 
     */
    public int getNrWorkers() { return this.nrWorkers; }
    
    /**
     * 
     * @return 
     */
    public int getNrWarmUps() { return this.nrWarmUps; }

    /**
     * 
     * @return 
     */
    public int[] getDataSizes() { return this.dataSizes; }

    /**
     * 
     * @return 
     */
    public int getNrWrites() { return this.nrWrites; }

    /**
     * 
     * @return 
     */
    public int getNrReads() { return this.nrReads; }

    /**
     * 
     * @return 
     */
    public String getFileName() { return this.fileName; }
    
    /**
     * 
     * @return 
     */
    public String getSide() { return this.side; }
    
    /**
     * 
     * @return 
     */
    public String getFilePath() { return this.filePath; }
    
}
