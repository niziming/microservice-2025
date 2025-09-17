package cn.zm.ddd.domain.model.order;

import cn.zm.ddd.shared.valueobject.EntityId;

/**
 * 订单ID值对象
 */
public record OrderId(String value) {
    
    public OrderId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("订单ID不能为空");
        }
    }
    
    public static OrderId generate() {
        return new OrderId(EntityId.generate().value());
    }
    
    public static OrderId of(String value) {
        return new OrderId(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}