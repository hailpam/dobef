
package org.pm.datagrid.benchmark.impl;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author pmaresca
 * @since
 * @version
 */
public class AmqConsumer implements Runnable
{
    private static final boolean DEBUG = false;
    
    private static final String USERNAME = "pmaresca";
    private static final String PASSWORD = "PMARESCA";
    
    private final int nrReads;
    
    private final String queue;

    
    public AmqConsumer(String queue, int nrReads) 
    {
        this.queue = queue;
        this.nrReads = nrReads;
    }
    
    
    @Override
    public void run() 
    {
        ActiveMQConnectionFactory conFactory = new ActiveMQConnectionFactory(
                                    ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
        
        try {
            conFactory.setUserName(USERNAME);
            conFactory.setPassword(PASSWORD);
            
            Connection connection = conFactory.createConnection();
            
            if(DEBUG) {
                System.out.println("URL: " +conFactory.getBrokerURL());
                System.out.println("Username/Password: " +conFactory.getUserName() 
                                                     +"/"
                                                     +conFactory.getPassword());
            }
            
            connection.start();
            
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queue);
            
            MessageConsumer consumer = session.createConsumer(destination);
            int itr = 0;
            
            do {
                Message message = consumer.receive();
                if(DEBUG) {
                    System.out.println("Received: " +((TextMessage) message).getText());
                }
                
                itr += 1;
            }while(itr < nrReads);
            
            consumer.close();
            connection.stop();
        } catch (JMSException ex) {
            System.err.println("Error: " +ex.toString());
            
            System.exit(-1);
        }
        
    }
    
    public static void main(String[] args) throws InterruptedException 
    {
        Thread tThread = new Thread(new AmqConsumer("test.queue", 1000000));
        tThread.start();
        tThread.join();
        
        System.exit(0);
    }

}
