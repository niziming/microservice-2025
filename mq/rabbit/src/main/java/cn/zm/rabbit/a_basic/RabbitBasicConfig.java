package cn.zm.rabbit.a_basic;

/**
 * @author Simon.ni
 */

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static cn.zm.rabbit.Constant.*;

@Configuration
public class RabbitBasicConfig {

    /**
     * 定义一个持久化队列
     */
    @Bean
    public Queue demoQueue() {
        // durable: true (持久化，重启不丢失)
        return new Queue(QUEUE_NAME, true);
    }

    /**
     * 定义一个 Direct 交换机
     */
    @Bean
    public DirectExchange demoExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    /**
     * 绑定：将队列绑定到交换机上，并指定 RoutingKey
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(demoQueue()).to(demoExchange()).with(ROUTING_KEY);
    }
}