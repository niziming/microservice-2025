package cn.zm.rabbit;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static cn.zm.rabbit.Constant.*;

/**
 * @author Simon.ni
 */
@Slf4j
@Tag(name = "RabbitMQ")
@AllArgsConstructor
@RestController
public class TestController {
    private final RabbitTemplate rabbitTemplate;

    @GetMapping("/ack")
    @Operation(summary = "ack消息")
    public String ack(String msg) {
        // 1. 发送 log.info -> 只有 QUEUE_ALL 能收到
        rabbitTemplate.convertAndSend(TOPIC_ACK_EXCHANGE, ROUTING_ACK_KEY, msg);

        return "Topic 消息发送完毕";
    }

    @Operation(summary = "不同类型消息订阅")
    @GetMapping("/sendTopic")
    public String sendTopic() {
        // 1. 发送 log.info -> 只有 QUEUE_ALL 能收到
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, "log.info", "这是一条普通日志");

        // 2. 发送 log.error -> QUEUE_ALL 和 QUEUE_ERROR 都能收到
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, "log.error", "这是一条报错日志");

        return "Topic 消息发送完毕";
    }

    @Operation(summary = "普通消息")
    @GetMapping("/send")
    public String send(@RequestParam String msg) {
        // 发送消息
        // 参数1: 交换机名称
        // 参数2: 路由键 (RoutingKey)
        // 参数3: 消息内容 (可以是对象，会自动序列化为 JSON)
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, msg);
        return "发送成功: " + msg;
    }
}