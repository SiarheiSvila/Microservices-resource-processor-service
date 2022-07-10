package com.epam.resourceprocessor.activemq;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.Message;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

@Component
@Slf4j
public class ResourceCreatedListener {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ResourceOrchestrator resourceOrchestrator;

    @Value("${spring.activemq.queues.resource-created}")
    private String resourceCreatedQueue;

    @Retryable(value = Exception.class)
    @JmsListener(destination = "${spring.activemq.queues.resource-created}", containerFactory = "queueListenerFactory")
    public void receiveMessageFromQueue(Message jsonMessage) throws JMSException, JsonProcessingException {
        ActiveMQObjectMessage message = (ActiveMQObjectMessage) jsonMessage;
        Long resourceId = (Long) message.getObject();
        log.info("Received resourceId: " + resourceId + " from queue - " + resourceCreatedQueue);
        resourceOrchestrator.processSongCreation(resourceId);
    }
}
