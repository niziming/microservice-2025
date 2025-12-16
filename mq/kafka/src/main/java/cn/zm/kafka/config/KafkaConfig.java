package cn.zm.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

/**
 * 如果业务逻辑报错了（比如数据库挂了），我们不希望消费者一直卡死在这条消息上，也不希望这条消息直接丢弃。
 * Spring Kafka 提供了非常强大的 DefaultErrorHandler。
 *
 * @author Simon.ni
 */
@Configuration
public class KafkaConfig {

    @Bean
    public CommonErrorHandler errorHandler(KafkaTemplate<Object, Object> template) {
        // 1. 定义死信队列恢复器
        // 当重试耗尽后，将消息发送到 "原Topic名称.DLT" (Dead Letter Topic)
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template);

        // 2. 定义重试策略
        // 每隔 1秒 重试一次，最多重试 3次
        FixedBackOff backOff = new FixedBackOff(1000L, 3);

        // 3. 构建错误处理器
        return new DefaultErrorHandler(recoverer, backOff);
    }
}