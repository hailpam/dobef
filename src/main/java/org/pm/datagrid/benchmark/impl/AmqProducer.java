
package org.pm.datagrid.benchmark.impl;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author pmaresca
 * @since
 * @version
 */
public class AmqProducer implements Runnable
{
    private static final boolean DEBUG = false;
    
    private static final String USERNAME = "pmaresca";
    private static final String PASSWORD = "PMARESCA";
    
    private final int nrWrites;
    private final int dataSize;
    
    private final String queue;

    public AmqProducer(int nrWrites, int dataSize, String queue) 
    {
        this.nrWrites = nrWrites;
        this.dataSize = dataSize;
        this.queue = queue;
    }
    
    @Override
    public void run() 
    {
        
        ActiveMQConnectionFactory conFactory = new ActiveMQConnectionFactory(
                                                    ActiveMQConnection.DEFAULT_BROKER_URL);
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
            
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            
            int itr = 0;
            do {
                String payload = RandomStringUtils.randomAlphanumeric(dataSize);
                TextMessage message = session.createTextMessage(payload);

                producer.send(message);
                
                itr += 1;
            }while(itr < nrWrites);
            
            session.close();
            connection.close();
        } catch (JMSException ex) {
            System.err.println("Error: " +ex.toString());
        } 
    }
    
    public static void main(String[] args) throws InterruptedException 
    {
        Thread tTest = new Thread(new AmqProducer(1000000, 256, "test.queue"));
        tTest.start();
        tTest.join();
        
        System.exit(0);
    }

}
