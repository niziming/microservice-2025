package cn.zm.rocket.c_delay;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import static cn.zm.rocket.Constant.GROUP;
import static cn.zm.rocket.Constant.TOPIC_DELAY;

/**
 * @author Simon.ni
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = TOPIC_DELAY,
        consumerGroup = GROUP,
        consumeMode = ConsumeMode.ORDERLY
)
public class DelayConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        System.out.printf(">>>>>> %s 延时消费 Consumer 收到消息: %s \n", this.getClass().getSimpleName(), s);
    }
}
