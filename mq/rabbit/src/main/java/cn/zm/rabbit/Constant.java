package cn.zm.rabbit;

/**
 * @author Simon.ni
 */
public class Constant {
    // 1. 定义队列名
    public static final String QUEUE_NAME = "boot.demo.queue";
    // 2. 定义交换机名
    public static final String EXCHANGE_NAME = "boot.demo.exchange";
    // 3. 定义路由键 (RoutingKey)
    public static final String ROUTING_KEY = "boot.demo.key";


    // 定义 Topic 交换机
    public static final String TOPIC_EXCHANGE = "boot.topic.exchange";
    // 队列 A：只关心 error 日志
    public static final String QUEUE_ERROR = "topic.error.queue";
    // 队列 B：关心所有日志
    public static final String QUEUE_ALL = "topic.all.queue";
    public static final String ERROR_ROUTING_KEY = "*.error";
    public static final String ALL_ROUTING_KEY = "log.#";

    public static final String QUEUE_ACK_NAME = "boot.ack.queue";
    public static final String TOPIC_ACK_EXCHANGE = "boot.ack.queue";
    public static final String ROUTING_ACK_KEY = "boot.demo.key";


}
