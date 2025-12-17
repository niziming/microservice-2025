package cn.zm.kafka.config;

import cn.zm.kafka.dto.UserRegisterEvent;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static cn.zm.kafka.constants.KafkaConstants.TOPIC_NAME;

/**
 * @author Simon.ni
 */
@Slf4j
@Component
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendUserRegisterMessage(UserRegisterEvent event) {
        // 1. 将对象转为 JSON 字符串
        String jsonMessage = JSON.toJSONString(event);

        System.out.printf("准备发送消息: %s", jsonMessage);

        // 2. 发送消息 (Topic, Key, Value)
        // Key 设置为 userId，可以保证同一个用户的消息进入同一个分区，保证顺序
        kafkaTemplate.send(TOPIC_NAME, event.getUserId().toString(), jsonMessage);
    }
}