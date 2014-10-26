
package org.pm.datagrid.benchmark;

import com.google.common.base.Preconditions;

/**
 *
 * @author pmaresca
 */
public class ConfigurationBuilder 
{
    private static final int NR_TESTS_DEFAULT = 10;
    private static final int NR_REPETITIONS_DEFAULT = 10;
    private static final int NR_WORKERS_DEFAULT = 1;
    private static final int[] DATA_SIZES_DEFAULT = { 256 };
    
    private int nrTests;
    private int nrRepetitions;
    private int nrWorkers;
    private int nrWarmUps;
    private int[] dataSizes;
    private int nrWrites;
    private int nrReads;
    
    private String fileName;
    private String filePath;
    private String side;

    
    public ConfigurationBuilder() 
    {
        this.nrTests = -1;
        this.nrRepetitions = -1;
        this.nrWorkers = -1;
        this.nrWrites = -1;
        this.nrReads = -1;
        this.fileName = "";
        this.filePath = "";
        this.side = "";
        this.nrWarmUps = 3;
    }
    
    public ConfigurationBuilder nrTests(int nrTests) 
    {
        Preconditions.checkArgument(nrTests > 0);
        this.nrTests = nrTests;
        return this;
    }
    
    public ConfigurationBuilder nrRepetitions(int nrRepetitions) 
    {
        Preconditions.checkArgument(nrRepetitions > 0);
        this.nrRepetitions = nrRepetitions;
        return this;
    }
    
    public ConfigurationBuilder nrWorkers(int nrWorkers) 
    {
        Preconditions.checkArgument(nrWorkers > 0);
        this.nrWorkers = nrWorkers;
        return this;
    }
    
    public ConfigurationBuilder nrWarmUps(int nrWarmUps) 
    {
        Preconditions.checkArgument(nrWarmUps >= 0);
        this.nrWarmUps = nrWarmUps;
        return this;
    }
    
    public ConfigurationBuilder dataSizes(int[] dataSizes) 
    {
        Preconditions.checkArgument(dataSizes.length > 0);
        this.dataSizes = dataSizes;
        return this;
    }
    
    public ConfigurationBuilder nrWrites(int nrWrites) 
    {
        Preconditions.checkArgument(nrWrites > 0);
        this.nrWrites = nrWrites;
        return this;
    }
    
    public ConfigurationBuilder nrReads(int nrReads) 
    {
        Preconditions.checkArgument(nrReads > 0);
        this.nrReads = nrReads;
        return this;
    }
    
    public ConfigurationBuilder fileName(String fileName) 
    {
        Preconditions.checkArgument(fileName != null && !fileName.equals(""));
        this.fileName = fileName;
        return this;
    }
    
    public ConfigurationBuilder filePath(String filePath) 
    {
        Preconditions.checkArgument(filePath != null && !filePath.equals(""));
        this.filePath = filePath;
        return this;
    }
    
    public ConfigurationBuilder side(String side) 
    {
        Preconditions.checkArgument(side != null && !side.equals(""));
        this.side = side;
        return this;
    }
    
    public Configuration build() throws BenchmarkException
    {
        if(this.nrTests == -1)
            this.nrTests = NR_TESTS_DEFAULT;
        if(this.nrRepetitions == -1)
            this.nrRepetitions = NR_REPETITIONS_DEFAULT;
        if(this.nrWorkers == -1)
            this.nrWorkers = NR_WORKERS_DEFAULT;
        if(this.dataSizes.length == 0)
            this.dataSizes = DATA_SIZES_DEFAULT;
        if(this.nrReads == -1)
            throw new BenchmarkException("Nr of READ Operation must be specified");
        if(this.nrReads == -1)
            throw new BenchmarkException("Nr of WRITE Operation must be specified");
        if(this.side.equals(""))
            throw new BenchmarkException("Side (Client or Server) must be specified");
        
        return new Configuration(this.nrTests, this.nrRepetitions, this.nrWorkers,
                                    this.nrWarmUps, this.dataSizes, this.nrWrites, 
                                    this.nrReads, this.fileName, this.filePath,  
                                    this.side);
    }
    
}
