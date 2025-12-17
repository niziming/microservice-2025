package cn.zm.rocket.a_basic;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import static cn.zm.rocket.Constant.GROUP;
import static cn.zm.rocket.Constant.TOPIC;

/**
 * @author Simon.ni
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = TOPIC, consumerGroup = GROUP)
public class BasicConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println("-------------------------------------------");
        System.out.println("Consumer 收到消息: " + message);
        System.out.println("-------------------------------------------");
    }
}