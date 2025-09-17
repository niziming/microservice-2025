package cn.zm.ddd.application.dto;

import cn.zm.ddd.domain.model.order.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单数据传输对象
 */
public record OrderDto(
    String id,
    String customerId,
    String status,
    String statusDescription,
    List<OrderItemDto> items,
    int itemCount,
    int totalQuantity,
    BigDecimal totalAmount,
    String currency,
    LocalDateTime createdAt,
    LocalDateTime lastModifiedAt
) {
    
    /**
     * 从领域对象转换为DTO
     */
    public static OrderDto from(Order order) {
        List<OrderItemDto> itemDtos = order.getItems().stream()
            .map(OrderItemDto::from)
            .toList();
        
        return new OrderDto(
            order.getId().value(),
            order.getCustomerId().value(),
            order.getStatus().name(),
            order.getStatus().getDescription(),
            itemDtos,
            order.getItemCount(),
            order.getTotalQuantity(),
            order.getTotalAmount().amount(),
            order.getTotalAmount().currency().getCurrencyCode(),
            order.getCreatedAt(),
            order.getLastModifiedAt()
        );
    }
}