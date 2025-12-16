package cn.zm.kafka.controller;


import cn.zm.kafka.config.KafkaProducer;
import cn.zm.kafka.dto.UserRegisterEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Simon.ni
 */
@Tag(name = "测试接口")
@RestController
@RequiredArgsConstructor
public class TestController {
    private final KafkaProducer kafkaProducer;

    @Operation(summary = "发送消息")
    @GetMapping("/send")
    public String send(@RequestParam String name) {
        UserRegisterEvent event = new UserRegisterEvent(1001L, name, name + "@example.com");
        kafkaProducer.sendUserRegisterMessage(event);
        return "消息发送成功！请查看控制台日志";
    }
}