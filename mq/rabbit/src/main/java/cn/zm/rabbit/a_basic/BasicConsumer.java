package cn.zm.rabbit.a_basic;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static cn.zm.rabbit.Constant.QUEUE_NAME;

/**
 * @author Simon.ni
 */
@Component
public class BasicConsumer {
    @RabbitListener(queues = QUEUE_NAME)
    private void receive(String msg) {
        System.out.println("================");
        System.out.printf(">>>>>> %s 消费者: %s", this.getClass().getSimpleName(), msg);
        System.out.println("================");
    }
}
