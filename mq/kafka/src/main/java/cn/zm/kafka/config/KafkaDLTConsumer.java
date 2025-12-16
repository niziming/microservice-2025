package cn.zm.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import static cn.zm.kafka.constants.KafkaConstants.*;

/**
 * @author Simon.ni
 */
@Slf4j
@Component
public class KafkaDLTConsumer {

    @KafkaListener(topics = TOPIC_NAME + DLT, groupId = GROUP_ID)
    public void handleDltMessage(ConsumerRecord<String, String> record,
                                 @Header(KafkaHeaders.DLT_EXCEPTION_MESSAGE) String exceptionMessage,
                                 @Header(KafkaHeaders.DLT_EXCEPTION_STACKTRACE) String exceptionStacktrace) {
        log.error("================= 🚨 发现死信消息 🚨 =================");

        // 1. 打印消息内容
        log.error("消息Key: {}", record.key());
        log.error("消息Value: {}", record.value());
        log.error("原始Topic: {}", record.topic());
        log.error("原始分区: {}", record.partition());

        // 2. 打印导致进入死信队列的异常原因 (这是最重要的!)
        log.error("异常原因: {}", exceptionMessage);
        // log.error("异常堆栈: {}", exceptionStacktrace); // 堆栈太长，按需打印

        // 3. 处理逻辑：通常是入库或发钉钉/邮件报警
        saveToErrorLogDatabase(record.value(), exceptionMessage);
        sendAlertToAdmin(record.value());

        log.error("================= 💀 死信处理完毕 💀 =================");
    }


    private void saveToErrorLogDatabase(String message, String reason) {
        // 模拟入库：INSERT INTO error_msg_table ...
        log.info(">> 已将异常消息存入数据库，等待人工排查");
    }

    private void sendAlertToAdmin(String message) {
        // 模拟发送报警
        log.info(">> 已发送钉钉/邮件报警通知");
    }
}