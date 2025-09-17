package cn.zm.ddd.domain.model.product;

import cn.zm.ddd.shared.valueobject.EntityId;

/**
 * 商品ID值对象
 */
public record ProductId(String value) {
    
    public ProductId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("商品ID不能为空");
        }
    }
    
    public static ProductId generate() {
        return new ProductId(EntityId.generate().value());
    }
    
    public static ProductId of(String value) {
        return new ProductId(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}