package vn.softdreams.easypos.integration.easyinvoice.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import vn.softdreams.easypos.config.RabbitMqProperties;
import vn.softdreams.easypos.integration.TaskLogIdEnqueueMessage;

@Service
public class EasyInvoiceProducer {

    private final Logger log = LoggerFactory.getLogger(EasyInvoiceProducer.class);
    private final RabbitMqProperties rabbitMqProperties;
    private final RabbitTemplate rabbitTemplate;
    private final String commonExchange;

    public EasyInvoiceProducer(RabbitMqProperties rabbitMqProperties, RabbitTemplate rabbitTemplate) {
        this.rabbitMqProperties = rabbitMqProperties;
        this.rabbitTemplate = rabbitTemplate;
        this.commonExchange = rabbitMqProperties.getProducer().getDirectExchange();
    }

    public void issueInvoice(TaskLogIdEnqueueMessage message) {
        log.info("Begin to produce issue invoice message: {}", message);
        String routingKey = rabbitMqProperties.getEasyInvoice().getIssueInvoiceRoutingKey();
        rabbitTemplate.convertAndSend(commonExchange, routingKey, message.getTaskLogId());
    }

    public void checkInvoice(TaskLogIdEnqueueMessage message) {
        log.info("Begin to produce check invoice message: {}", message);
        String routingKey = rabbitMqProperties.getEasyInvoice().getCheckInvoiceRoutingKey();
        rabbitTemplate.convertAndSend(commonExchange, routingKey, message.getTaskLogId());
    }

    public void importInvoice(TaskLogIdEnqueueMessage message) {
        log.info("Begin to produce check invoice message: {}", message);
        String routingKey = rabbitMqProperties.getEasyInvoice().getImportInvoiceRoutingKey();
        rabbitTemplate.convertAndSend(commonExchange, routingKey, message.getTaskLogId());
    }

    public void replaceInvoice(TaskLogIdEnqueueMessage message) {
        log.info("Begin to produce replace invoice message: {}", message);
        String routingKey = rabbitMqProperties.getEasyInvoice().getReplaceInvoiceRoutingKey();
        rabbitTemplate.convertAndSend(commonExchange, routingKey, message.getTaskLogId());
    }

    public void cancelInvoice(TaskLogIdEnqueueMessage message) {
        log.info("Begin to produce cancel invoice message: {}", message);
        String routingKey = rabbitMqProperties.getEasyInvoice().getCancelInvoiceRoutingKey();
        rabbitTemplate.convertAndSend(commonExchange, routingKey, message.getTaskLogId());
    }
}
