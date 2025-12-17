package cn.zm.kafka.config;

import cn.hutool.json.JSONUtil;
import cn.zm.kafka.dto.UserRegisterEvent;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static cn.zm.kafka.constants.KafkaConstants.GROUP_ID;
import static cn.zm.kafka.constants.KafkaConstants.TOPIC_NAME;

/**
 * @author Simon.ni
 */
@Slf4j
@Component
public class KafkaConsumer {

    @KafkaListener(topics = TOPIC_NAME, groupId = GROUP_ID)
    public void handleUserRegister(List<String> msgs, Acknowledgment ack) {
        // 1. 将 JSON 字符串转回对象
        System.out.printf("消费者收到消息 -> 批量用户数据: %s ", JSONUtil.toJsonPrettyStr(msgs));
        // --- 模拟异常场景 ---
        // if (event.getUsername().contains("error")) {
        //     log.error("检测到非法用户，准备抛出异常触发重试...");
        //     throw new RuntimeException("模拟业务处理失败！数据库连接超时！");
        // }
        // ------------------
        // 2. 模拟业务逻辑
        sendWelcomeEmail(msgs);

        // 3. 业务成功后，手动提交 Offset
        ack.acknowledge();
        System.out.println("Offset 已提交");
    }

    private void sendWelcomeEmail(List<String> msgs) {
        System.out.printf("正在给邮箱 %s 发送欢迎邮件...", msgs);
        // 模拟耗时
        try {
            Thread.sleep(100);
            List<UserRegisterEvent> collect = msgs.stream().map(msg -> JSON.parseObject(msg, UserRegisterEvent.class)).collect(Collectors.toList());
            System.out.printf("批量用户数据:%s ", JSONUtil.toJsonPrettyStr(collect));
        } catch (InterruptedException e) {
        }
        System.out.println("邮件发送成功！");
    }
}