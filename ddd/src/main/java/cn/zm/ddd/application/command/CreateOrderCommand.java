package cn.zm.ddd.application.command;

/**
 * 创建订单命令
 */
public record CreateOrderCommand(
    String customerId
) {
    
    public CreateOrderCommand {
        if (customerId == null || customerId.trim().isEmpty()) {
            throw new IllegalArgumentException("客户ID不能为空");
        }
    }
}