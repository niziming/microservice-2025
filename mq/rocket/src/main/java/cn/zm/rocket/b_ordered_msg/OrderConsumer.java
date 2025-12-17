package cn.zm.rocket.b_ordered_msg;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import static cn.zm.rocket.Constant.GROUP_ORDERLY;
import static cn.zm.rocket.Constant.TOPIC_ORDERLY;

/**
 * @author Simon.ni
 */
@Slf4j
@Component
@RocketMQMessageListener(
        topic = TOPIC_ORDERLY,
        consumerGroup = GROUP_ORDERLY,
        consumeMode = ConsumeMode.ORDERLY
)
public class OrderConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        System.out.printf(">>>>>> %s 顺序消费 Consumer 收到消息: %s \n", this.getClass().getSimpleName(), s);
    }
}
