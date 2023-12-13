package vn.softdreams.easypos.integration.easyinvoice.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import vn.softdreams.easypos.config.RabbitMqProperties;
import vn.softdreams.easypos.integration.TaskLogIdEnqueueMessage;

@Service
public class NgoGiaPhatInvoiceProducer {

    private final Logger log = LoggerFactory.getLogger(NgoGiaPhatInvoiceProducer.class);
    private final RabbitMqProperties rabbitMqProperties;
    private final RabbitTemplate rabbitTemplate;
    private final String commonExchange;

    public NgoGiaPhatInvoiceProducer(RabbitMqProperties rabbitMqProperties, RabbitTemplate rabbitTemplate) {
        this.rabbitMqProperties = rabbitMqProperties;
        this.rabbitTemplate = rabbitTemplate;
        this.commonExchange = rabbitMqProperties.getProducer().getDirectExchange();
    }

    public void issueInvoice(TaskLogIdEnqueueMessage message) {
        log.info("Begin to produce issue invoice message: {}", message);
        String routingKey = rabbitMqProperties.getNgoGiaPhatInvoice().getNgpIssueInvoiceRoutingKey();
        rabbitTemplate.convertAndSend(commonExchange, routingKey, message.getTaskLogId());
    }

    public void checkInvoice(TaskLogIdEnqueueMessage message) {
        log.info("Begin to produce check invoice message: {}", message);
        String routingKey = rabbitMqProperties.getNgoGiaPhatInvoice().getNgpCheckInvoiceRoutingKey();
        rabbitTemplate.convertAndSend(commonExchange, routingKey, message.getTaskLogId());
    }

    public void importInvoice(TaskLogIdEnqueueMessage message) {
        log.info("Begin to produce check invoice message: {}", message);
        String routingKey = rabbitMqProperties.getNgoGiaPhatInvoice().getNgpImportInvoiceRoutingKey();
        rabbitTemplate.convertAndSend(commonExchange, routingKey, message.getTaskLogId());
    }

    public void replaceInvoice(TaskLogIdEnqueueMessage message) {
        log.info("Begin to produce replace invoice message: {}", message);
        String routingKey = rabbitMqProperties.getNgoGiaPhatInvoice().getNgpReplaceInvoiceRoutingKey();
        rabbitTemplate.convertAndSend(commonExchange, routingKey, message.getTaskLogId());
    }

    public void cancelInvoice(TaskLogIdEnqueueMessage message) {
        log.info("Begin to produce cancel invoice message: {}", message);
        String routingKey = rabbitMqProperties.getNgoGiaPhatInvoice().getNgpCancelInvoiceRoutingKey();
        rabbitTemplate.convertAndSend(commonExchange, routingKey, message.getTaskLogId());
    }
}
