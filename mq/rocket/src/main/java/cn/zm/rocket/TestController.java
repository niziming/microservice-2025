package cn.zm.rocket;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.zm.rocket.Constant.*;

/**
 * @author Simon.ni
 */
@Slf4j
@Tag(name = "RocketMQ")
@AllArgsConstructor
@RestController
public class TestController {
    private final RocketMQTemplate rocketMQTemplate;

    @Operation(summary = "4_确认消息")
    @GetMapping("/sendAck")
    public String sendAck() {
        // 模拟订单id
        Message<String> msg = MessageBuilder.withPayload("订单超时关闭通知").build();

        // 发送消息
        // 参数3: timeout (同步发送超时时间)
        // 参数4: delayLevel (延时等级)。3 代表 10s，16 代表 30m
        // 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        rocketMQTemplate.syncSend(TOPIC_ACK, msg, 3000, 1);

        return "延时消息已发送，请等待 秒...";
    }

    @Operation(summary = "3_延时消息")
    @GetMapping("/sendDelay")
    public String sendDelay() {
        // 模拟订单id
        Message<String> msg = MessageBuilder.withPayload("订单超时关闭通知").build();

        // 发送消息
        // 参数3: timeout (同步发送超时时间)
        // 参数4: delayLevel (延时等级)。3 代表 10s，16 代表 30m
        // 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        rocketMQTemplate.syncSend(TOPIC_DELAY, msg, 3000, 2);

        return "延时消息已发送，请等待 秒...";
    }

    @Operation(summary = "2_顺序消息")
    @GetMapping("/sendOrder")
    public String sendOrder() {
        // 模拟订单id
        String orderId = "1001";

        // 发送消息
        rocketMQTemplate.syncSendOrderly(Constant.TOPIC_ORDERLY, "订单创建", orderId);
        rocketMQTemplate.syncSendOrderly(Constant.TOPIC_ORDERLY, "订单支付", orderId);
        rocketMQTemplate.syncSendOrderly(Constant.TOPIC_ORDERLY, "订单发货", orderId);

        return "发送成功: " + orderId;
    }

    @Operation(summary = "1_普通消息")
    @GetMapping("/send")
    public String send() {
        // 构建消息内容
        String msg = "Hello RocketMQ " + System.currentTimeMillis();

        // 发送消息
        // 参数1: topic
        // 参数2: 消息内容
        rocketMQTemplate.convertAndSend(TOPIC, msg);

        return "发送成功: " + msg;
    }
}