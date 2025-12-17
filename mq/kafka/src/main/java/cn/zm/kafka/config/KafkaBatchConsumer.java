package cn.zm.kafka.config;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

import static cn.zm.kafka.constants.KafkaConstants.GROUP_BATCH_ID;
import static cn.zm.kafka.constants.KafkaConstants.TOPIC_BATCH_NAME;

/**
 * @author Simon.ni
 */
@Slf4j
@Component
public class KafkaBatchConsumer {

    @KafkaListener(topics = TOPIC_BATCH_NAME, groupId = GROUP_BATCH_ID)
    public void handleUserBatchRegister(List<String> msgs, Acknowledgment ack) {
        // 1. 将 JSON 字符串转回对象

        System.out.printf("消费者收到消息 -> 用户ID: %s", JSONUtil.toJsonPrettyStr(msgs));

        // 2. 模拟业务逻辑
        sendWelcomeEmail("event.getEmail()");

        // 3. 业务成功后，手动提交 Offset
        ack.acknowledge();
        System.out.println("Offset 已提交");
    }

    private void sendWelcomeEmail(String email) {
        System.out.printf("正在给邮箱 %s 发送欢迎邮件...", email);
        // 模拟耗时
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        System.out.println("邮件发送成功！");
    }
}