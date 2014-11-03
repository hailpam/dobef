
package org.pm.datagrid.benchmark;

/**
 * Benchmark Exception.
 * 
 * @author Paolo Maresca <plo.maresca@gmail.com>
 */
public class BenchmarkException extends Exception
{
    /**
     * Create a <code>BenchmarkException</code> using the message provided in Input.
     * 
     * @param message   Error message
     */
    public BenchmarkException(String message) 
    {
        super(message);
    }

    /**
     * Create a <code>BenchmarkException</code> using both message and throwable
     * provided in Input.
     * 
     * @param message   Error message
     * @param cause     Error cause
     */
    public BenchmarkException(String message, Throwable cause) 
    {
        super(message, cause);
    }
    
}
