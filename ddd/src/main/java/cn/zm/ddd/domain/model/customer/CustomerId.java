package cn.zm.ddd.domain.model.customer;

import cn.zm.ddd.shared.valueobject.EntityId;

/**
 * 客户ID值对象
 * 
 * 继承通用EntityId，提供类型安全的客户标识符
 */
public record CustomerId(String value) {
    
    public CustomerId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("客户ID不能为空");
        }
    }
    
    public static CustomerId generate() {
        return new CustomerId(EntityId.generate().value());
    }
    
    public static CustomerId of(String value) {
        return new CustomerId(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}