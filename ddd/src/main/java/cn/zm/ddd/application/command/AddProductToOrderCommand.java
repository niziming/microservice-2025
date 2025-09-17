package cn.zm.ddd.application.command;

/**
 * 添加商品到订单命令
 */
public record AddProductToOrderCommand(
    String orderId,
    String productId,
    int quantity
) {
    
    public AddProductToOrderCommand {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("订单ID不能为空");
        }
        
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("商品ID不能为空");
        }
        
        if (quantity <= 0) {
            throw new IllegalArgumentException("商品数量必须大于0");
        }
    }
}