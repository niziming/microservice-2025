package cn.zm.rocket.d_ack;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import static cn.zm.rocket.Constant.GROUP_ACK;
import static cn.zm.rocket.Constant.TOPIC_ACK;

/**
 * @author Simon.ni
 */
@Component
@RocketMQMessageListener(
        topic = TOPIC_ACK,
        consumerGroup = GROUP_ACK
)
// 注意：这里实现 RocketMQListener<MessageExt> 可以拿到更多元数据
public class AckConsumer implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt message) {
        String body = new String(message.getBody());
        System.out.println("收到消息: " + body);

        try {
            // 模拟业务逻辑
            if (body.contains("fail")) {
                throw new RuntimeException("模拟业务报错");
            }

            // 业务成功，方法正常结束，Spring 会自动发送 ACK
            System.out.println("业务处理成功");

        } catch (Exception e) {
            // 捕获异常后，如果不抛出，Spring 认为消费成功了！
            // ★★★ 必须抛出异常，RocketMQ 才会重试 ★★★
            System.out.println("业务处理失败，准备重试...");
            throw new RuntimeException("申请重试");
        }
    }
}