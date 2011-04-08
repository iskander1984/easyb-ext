package org.easyb.plugin.jms

import org.springframework.context.support.ClassPathXmlApplicationContext
import groovy.xml.MarkupBuilder

class JMSCategories{
    static String sendAndReceive(Object self, String inputQueueName, String outputQueueName, String message, long timeout = 0){
        self.jmsTemplate.setReceiveTimeout(timeout)
        self.jmsTemplate.convertAndSend(inputQueueName, message)
        self.jmsTemplate.receiveAndConvert(outputQueueName)
    }
    static void send(Object self, String inputQueueName, String message){
        self.jmsTemplate.convertAndSend(inputQueueName, message)
    }
    
    static String receive(Object self, String outputQueueName, long timeout = 0){
        self.jmsTemplate.setReceiveTimeout(timeout)
        self.jmsTemplate.receiveAndConvert(outputQueueName)
    }
}