package vn.softdreams.easypos.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

@EnableRabbit
@Configuration
public class RabbitMqConfiguration {

    private final RabbitMqProperties rabbitMqProperties;

    public RabbitMqConfiguration(RabbitMqProperties rabbitMqProperties) {
        this.rabbitMqProperties = rabbitMqProperties;
    }

    // Định nghĩa các queue cần dùng
    @Bean
    public Queue easyInvoiceIssueInvoiceQueue() {
        return new Queue(rabbitMqProperties.getEasyInvoice().getIssueInvoiceQueue());
    }

    @Bean
    public Queue easyInvoiceCheckInvoiceQueue() {
        return new Queue(rabbitMqProperties.getEasyInvoice().getCheckInvoiceQueue());
    }

    @Bean
    public Queue easyInvoiceReplaceInvoiceQueue() {
        return new Queue(rabbitMqProperties.getEasyInvoice().getReplaceInvoiceQueue());
    }

    @Bean
    public Queue easyInvoiceImportInvoiceQueue() {
        return new Queue(rabbitMqProperties.getEasyInvoice().getImportInvoiceQueue());
    }

    @Bean
    public Queue easybooks88SendQueue() {
        return new Queue(rabbitMqProperties.getEasyBooks88().getSendQueue());
    }

    // Binding các queue trên với routing-key, để khi gửi chỉ cần truyền routing-key, không cần define queue
    // Định nghĩa exchange trước, RabbitMQ đang cấu hình tất cả đều dùng Direct exchange
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(rabbitMqProperties.getProducer().getDirectExchange());
    }

    @Bean
    public Queue ngpInvoiceIssueInvoiceQueue() {
        return new Queue(rabbitMqProperties.getNgoGiaPhatInvoice().getNgpIssueInvoiceQueue());
    }

    @Bean
    public Queue ngpInvoiceCheckInvoiceQueue() {
        return new Queue(rabbitMqProperties.getNgoGiaPhatInvoice().getNgpCheckInvoiceQueue());
    }

    @Bean
    public Queue ngpInvoiceReplaceInvoiceQueue() {
        return new Queue(rabbitMqProperties.getNgoGiaPhatInvoice().getNgpReplaceInvoiceQueue());
    }

    @Bean
    public Queue ngpInvoiceImportInvoiceQueue() {
        return new Queue(rabbitMqProperties.getNgoGiaPhatInvoice().getNgpImportInvoiceQueue());
    }

    @Bean
    public Binding easyInvoiceIssueInvoiceBinding() {
        return BindingBuilder
            .bind(easyInvoiceIssueInvoiceQueue())
            .to(directExchange())
            .with(rabbitMqProperties.getEasyInvoice().getIssueInvoiceRoutingKey());
    }

    @Bean
    public Binding easyInvoiceCheckInvoiceBinding() {
        return BindingBuilder
            .bind(easyInvoiceCheckInvoiceQueue())
            .to(directExchange())
            .with(rabbitMqProperties.getEasyInvoice().getCheckInvoiceRoutingKey());
    }

    @Bean
    public Binding easyInvoiceReplaceInvoiceBinding() {
        return BindingBuilder
            .bind(easyInvoiceReplaceInvoiceQueue())
            .to(directExchange())
            .with(rabbitMqProperties.getEasyInvoice().getReplaceInvoiceRoutingKey());
    }

    @Bean
    public Binding easyInvoiceImportInvoiceBinding() {
        return BindingBuilder
            .bind(easyInvoiceImportInvoiceQueue())
            .to(directExchange())
            .with(rabbitMqProperties.getEasyInvoice().getImportInvoiceRoutingKey());
    }

    @Bean
    public Binding easybooks88SendBinding() {
        return BindingBuilder
            .bind(easybooks88SendQueue())
            .to(directExchange())
            .with(rabbitMqProperties.getEasyBooks88().getSendRoutingKey());
    }

    @Bean
    public Binding ngpInvoiceIssueInvoiceBinding() {
        return BindingBuilder
            .bind(ngpInvoiceImportInvoiceQueue())
            .to(directExchange())
            .with(rabbitMqProperties.getNgoGiaPhatInvoice().getNgpIssueInvoiceRoutingKey());
    }

    @Bean
    public Binding ngpInvoiceCheckInvoiceBinding() {
        return BindingBuilder
            .bind(ngpInvoiceCheckInvoiceQueue())
            .to(directExchange())
            .with(rabbitMqProperties.getNgoGiaPhatInvoice().getNgpCheckInvoiceRoutingKey());
    }

    @Bean
    public Binding ngpInvoiceReplaceInvoiceBinding() {
        return BindingBuilder
            .bind(ngpInvoiceReplaceInvoiceQueue())
            .to(directExchange())
            .with(rabbitMqProperties.getNgoGiaPhatInvoice().getNgpReplaceInvoiceRoutingKey());
    }

    @Bean
    public Binding ngpInvoiceImportInvoiceBinding() {
        return BindingBuilder
            .bind(ngpInvoiceImportInvoiceQueue())
            .to(directExchange())
            .with(rabbitMqProperties.getNgoGiaPhatInvoice().getNgpImportInvoiceRoutingKey());
    }

    @Bean
    public MessageConverter defaultMessageConverter() {
        return new Jackson2JsonMessageConverter(new ObjectMapper());
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setVirtualHost(rabbitMqProperties.getVirtualHost());
        connectionFactory.setHost(rabbitMqProperties.getHost());
        connectionFactory.setUsername(rabbitMqProperties.getUsername());
        connectionFactory.setPassword(rabbitMqProperties.getPassword());
        return connectionFactory;
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(defaultMessageConverter());
        rabbitTemplate.setReplyTimeout(rabbitMqProperties.getProducer().getReplyTimeout());
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setMessageConverter(defaultMessageConverter());
        factory.setConcurrentConsumers(rabbitMqProperties.getConsumer().getConcurrentConsumer());
        factory.setMaxConcurrentConsumers(rabbitMqProperties.getConsumer().getMaxConcurrentConsumer());
        factory.setPrefetchCount(rabbitMqProperties.getConsumer().getPrefetchCount());
        factory.setErrorHandler(errorHandler());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    @Bean
    public ErrorHandler errorHandler() {
        return new ConditionalRejectingErrorHandler(new MyFatalExceptionStrategy());
    }

    public static class MyFatalExceptionStrategy extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {

        private final Logger logger = LoggerFactory.getLogger(MyFatalExceptionStrategy.class);

        @Override
        public boolean isFatal(Throwable t) {
            if (t instanceof ListenerExecutionFailedException) {
                ListenerExecutionFailedException lefe = (ListenerExecutionFailedException) t;
                logger.error(
                    "Failed to process inbound message from queue " +
                    lefe.getFailedMessage().getMessageProperties().getConsumerQueue() +
                    "; failed message: " +
                    lefe.getFailedMessage(),
                    t
                );
            }
            return super.isFatal(t);
        }
    }
}
