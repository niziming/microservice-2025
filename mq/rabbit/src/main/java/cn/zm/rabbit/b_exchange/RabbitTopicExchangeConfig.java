package cn.zm.rabbit.b_exchange;

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
public class RabbitTopicExchangeConfig {
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Queue errorQueue() {
        return new Queue(QUEUE_ERROR);
    }

    @Bean
    public Queue allQueue() {
        return new Queue(QUEUE_ALL);
    }

    // 绑定 A：RoutingKey 包含 error 的 (例如 log.error, user.error)
    @Bean
    public Binding bindingError() {
        return BindingBuilder.bind(errorQueue()).to(topicExchange()).with(ERROR_ROUTING_KEY);
    }

    // 绑定 B：所有 log 开头的 (例如 log.info, log.error, log.warn)
    @Bean
    public Binding bindingAll() {
        return BindingBuilder.bind(allQueue()).to(topicExchange()).with(ALL_ROUTING_KEY);
    }
}