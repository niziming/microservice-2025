package cn.zm.ddd.application.dto;

import cn.zm.ddd.domain.model.order.OrderItem;

import java.math.BigDecimal;

/**
 * 订单项数据传输对象
 */
public record OrderItemDto(
    String productId,
    String productName,
    BigDecimal unitPrice,
    String currency,
    int quantity,
    BigDecimal subtotal
) {
    
    /**
     * 从领域对象转换为DTO
     */
    public static OrderItemDto from(OrderItem orderItem) {
        return new OrderItemDto(
            orderItem.productId().value(),
            orderItem.productName(),
            orderItem.unitPrice().amount(),
            orderItem.unitPrice().currency().getCurrencyCode(),
            orderItem.quantity(),
            orderItem.calculateSubtotal().amount()
        );
    }
}