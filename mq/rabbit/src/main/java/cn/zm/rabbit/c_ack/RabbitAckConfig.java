package cn.zm.rabbit.c_ack;

/**
 * @author Simon.ni
 */

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cn.zm.rabbit.Constant.*;

@Configuration
public class RabbitAckConfig {
    @Bean
    public TopicExchange ackExchange() {
        return new TopicExchange(TOPIC_ACK_EXCHANGE);
    }

    @Bean
    public Queue ackQueue() {
        return new Queue(QUEUE_ACK_NAME);
    }

    // 绑定 B：所有 log 开头的 (例如 log.info, log.error, log.warn)
    @Bean
    public Binding bindingAck() {
        return BindingBuilder.bind(ackQueue()).to(ackExchange()).with(ROUTING_ACK_KEY);
    }
}