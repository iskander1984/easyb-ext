package org.easyb.plugin.jms

import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.jms.core.MessageCreator
import groovy.xml.MarkupBuilder
import java.util.UUID

class JMSCategories{
    
    static String createCorrelationId(Object self) { UUID.randomUUID().toString() }
 
    static String sendAndReceive(Object self, String inputQueueName, String outputQueueName, String message, long timeout = 0){
        self.jmsTemplate.setReceiveTimeout(timeout)
        String correlationId = createCorrelationId(self)
        send(self, inputQueueName, message, correlationId)
        return receive(self, outputQueueName, correlationId, timeout)
    }
    
    static UUID send(Object self, String inputQueueName, String message, correlationId){
        def messageCreator = { session ->
                def textMessage = session.createTextMessage(message)
                textMessage.setJMSCorrelationID correlationId
                textMessage
        } as MessageCreator
        self.jmsTemplate.send(inputQueueName, messageCreator)
        
    }
        
    static String receive(Object self, String outputQueueName, String correlationId, long timeout = 0){
        self.jmsTemplate.setReceiveTimeout(timeout)
        def selector = createSelectorFrom(correlationId)
        def message  = self.jmsTemplate.receiveAndConvert(outputQueueName)
        message   
    }
    
    static String createSelectorFrom(correlationId) {
        "JMSCorrelationID='$correlationId'"
    }
}