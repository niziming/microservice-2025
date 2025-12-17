package cn.zm.rabbit.b_exchange;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static cn.zm.rabbit.Constant.QUEUE_ALL;
import static cn.zm.rabbit.Constant.QUEUE_ERROR;

/**
 * @author Simon.ni
 */
@Component
public class TopicExchangeConsumer {
    @RabbitListener(queues = QUEUE_ERROR)
    private void error(String msg) {
        System.out.println("================");
        System.out.printf(">>>>>> %s 消费者订阅报错信息: %s \n", this.getClass().getSimpleName(), msg);
        System.out.println("================");
    }

    @RabbitListener(queues = QUEUE_ALL)
    private void all(String msg) {
        System.out.println("================");
        System.out.printf(">>>>>> %s 消费者订阅所有信息: %s \n", this.getClass().getSimpleName(), msg);
        System.out.println("================");
    }
}
