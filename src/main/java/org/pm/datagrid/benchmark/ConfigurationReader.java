
package org.pm.datagrid.benchmark;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * 
 * @author pmaresca
 */
public class ConfigurationReader 
{
    public static Map<String, Configuration> readConfiguration(InputStream is) 
                                                throws IOException,
                                                       IllegalStateException,
                                                       BenchmarkException
    {
        Preconditions.checkNotNull(is);
        
        Map<String, Configuration> configs = new HashMap<>();
        
        Properties props = new Properties();
        props.load(is);
        
        String[] sides = props.getProperty("benchmark.settings.side").split(",");
        String[] fileNames = props.getProperty("benchmark.settings.results.file.name").split(",");
        if(sides.length != fileNames.length)
            throw new IllegalStateException("Sides and File Names should have the "
                                                + "same number of Entries");
        
        String[] dataSizes = props.getProperty("benchmark.settings.data.size").split(",");
        if(dataSizes.length == 0)
            throw new IllegalStateException("Data Sizes need to be specified");
        int[] sizes = new int[dataSizes.length];
        for(int i = 0; i < dataSizes.length; i++) {
            sizes[i] = Integer.parseInt(dataSizes[i]);
        }
        
        for(int i = 0; i < sides.length; i++) {
            Configuration conf = null; 
            conf = new ConfigurationBuilder().fileName(fileNames[i])
                            .side(sides[i])
                            .dataSizes(sizes)
                            .filePath(props.getProperty("benchmark.settings.results.file.path"))
                            .nrTests(Integer.parseInt(
                                        props.getProperty("benchmark.settings.number.tests")))
                            .nrRepetitions(Integer.parseInt(
                                        props.getProperty("benchmark.settings.number.repetitions")))
                            .nrWarmUps(Integer.parseInt(
                                        props.getProperty("benchmark.settings.number.warmups")))
                            .nrWorkers(Integer.parseInt(
                                        props.getProperty("benchmark.settings.number.workers")))
                            .nrReads(Integer.parseInt(
                                        props.getProperty("benchmark.settings.number.reads")))
                            .nrWrites(Integer.parseInt(
                                        props.getProperty("benchmark.settings.number.writes")))
                            .build();
            configs.put(sides[i], conf);
        }
        
        return configs;
    }
}
