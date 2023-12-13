package vn.softdreams.easypos.integration.easybooks88.queue;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import vn.softdreams.easypos.domain.TaskLog;
import vn.softdreams.easypos.repository.TaskLogRepository;

import java.util.Optional;

@Component
@RabbitListener(queues = "send-eb88", id = "send-eb88-listener", containerFactory = "rabbitListenerContainerFactory")
public class EB88Listener {

    private final Logger log = LoggerFactory.getLogger(EB88Listener.class);
    private final TaskLogRepository taskLogRepository;
    private final EB88MessageHandler eb88MessageHandler;

    public EB88Listener(TaskLogRepository taskLogRepository, EB88MessageHandler eb88MessageHandler) {
        this.taskLogRepository = taskLogRepository;
        this.eb88MessageHandler = eb88MessageHandler;
    }

    @RabbitHandler
    public void receiveMessage(Message raw, Integer taskLogId, Channel channel) throws Exception {
        Thread.sleep(5000);
        Optional<TaskLog> taskLogOptional = taskLogRepository.findById(taskLogId);
        if (taskLogOptional.isPresent()) {
            TaskLog taskLog = taskLogOptional.get();

            // Gọi hàm xử lý ở eb88MessageHandler
            try {
                eb88MessageHandler.process(taskLog);
            } catch (Exception e) {
                log.error("Failed to process messageId {} : {}", taskLogId, e.getMessage());
            }
            try {
                channel.basicAck(raw.getMessageProperties().getDeliveryTag(), true);
            } catch (Exception e) {
                log.error("Failed to ACK message, taskLogId = {} \n queueMessageInfo = {}", taskLogId, raw.getMessageProperties());
            }
        } else {
            log.error("Tasklog {} is not found", taskLogId);
            return;
        }
    }
}
