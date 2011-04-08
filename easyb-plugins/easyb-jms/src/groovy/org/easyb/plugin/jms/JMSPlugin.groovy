package org.easyb.plugin.jms

import org.easyb.plugin.BasePlugin
import org.easyb.plugin.jms.JMSCategories

import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.jms.core.JmsTemplate

import groovy.xml.MarkupBuilder

class JMSPlugin extends BasePlugin {
  static final String DEFAULT_SPRING_CONTEXT_FILE = "easyb-jms-context.xml"
  static final String DFAULT_JMS_TEMPLATE_BEAN_NAME = "jmsTemplate"
  JmsTemplate jmsTemplate 
   
  public String getName() {
    return "jms"
  }
  
  public Object beforeStory(Binding binding) {
      Object.mixin JMSCategories   
      def context = new ClassPathXmlApplicationContext(resolveSpringContextFileName(binding))
      jmsTemplate = context.getBean(DFAULT_JMS_TEMPLATE_BEAN_NAME)
      binding.jmsTemplate = jmsTemplate
  }
  
  private resolveSpringContextFileName(Binding binding){
      String springContexFileName
      if (binding.springContextFileName){
          springContexFileName = binding.springContextFileName
      }else{
          springContexFileName = DEFAULT_SPRING_CONTEXT_FILE
      }
  }
}