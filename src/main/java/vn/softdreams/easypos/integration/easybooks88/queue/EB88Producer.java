package vn.softdreams.easypos.integration.easybooks88.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.softdreams.easypos.config.RabbitMqProperties;
import vn.softdreams.easypos.integration.TaskLogIdEnqueueMessage;

@Service
public class EB88Producer {

    private final Logger log = LoggerFactory.getLogger(EB88Producer.class);
    private final RabbitMqProperties rabbitMqProperties;
    private final RabbitTemplate rabbitTemplate;
    private final String commonExchange;

    public EB88Producer(RabbitMqProperties rabbitMqProperties, RabbitTemplate rabbitTemplate) {
        this.rabbitMqProperties = rabbitMqProperties;
        this.rabbitTemplate = rabbitTemplate;
        this.commonExchange = rabbitMqProperties.getProducer().getDirectExchange();
    }

    @Async
    public void send(TaskLogIdEnqueueMessage message) {
        log.info("Begin to send eb message: {}", message);
        String routingKey = rabbitMqProperties.getEasyBooks88().getSendRoutingKey();
        rabbitTemplate.convertAndSend(commonExchange, routingKey, message.getTaskLogId());
    }
}
