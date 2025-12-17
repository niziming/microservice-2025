package cn.zm.rabbit.c_ack;

/**
 * @author Simon.ni
 */

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static cn.zm.rabbit.Constant.QUEUE_ACK_NAME;

@Component
public class AckConsumer {

    @RabbitListener(queues = QUEUE_ACK_NAME)
    public void receive(String msg, Channel channel, Message message) throws IOException {
        // 每个消息都有一个唯一的 deliveryTag
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            System.out.println("正在处理业务: " + msg);

            // 模拟业务报错
            if (msg.contains("fail")) {
                throw new RuntimeException("业务异常");
            }

            // 1. 业务成功：手动 ACK
            // false 表示只确认当前这一条，不批量确认
            channel.basicAck(deliveryTag, false);
            System.out.println("业务处理成功，已 ACK");

        } catch (Exception e) {
            System.out.println("业务处理失败，准备拒收...");

            // 2. 业务失败：手动 NACK (拒收)
            // 参数3 (requeue): true 表示重新放回队列（会一直重试），false 表示丢弃或进死信队列
            // 生产环境通常设为 false 并记录日志，或者配合死信队列使用
            channel.basicNack(deliveryTag, false, false);
        }
    }
}