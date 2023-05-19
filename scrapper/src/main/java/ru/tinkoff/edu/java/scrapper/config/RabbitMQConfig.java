package ru.tinkoff.edu.java.scrapper.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.service.queue.ScrapperQueueProducer;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
public class RabbitMQConfig {
    private final String exchangeName;
    private final String queueName;
    private final String routingKey;

    private final static String DLQ_SUFFIX = ".dlq";

    public RabbitMQConfig(ApplicationConfig config) {
        this.exchangeName = config.getRabbitQueue().getExchangeName();
        this.queueName = config.getRabbitQueue().getQueueName();
        this.routingKey = config.getRabbitQueue().getRoutingKey();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue queue() {
        return QueueBuilder
            .durable(queueName)
            .withArgument("x-dead-letter-exchange", exchangeName + DLQ_SUFFIX)
            .withArgument("x-dead-letter-routing-key", routingKey + DLQ_SUFFIX)
            .build();
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(routingKey);
    }

    @Bean
    public ScrapperQueueProducer scrapperQueueProducer(RabbitTemplate rabbitTemplate, ApplicationConfig config) {
        return new ScrapperQueueProducer(rabbitTemplate, config);
    }
}
