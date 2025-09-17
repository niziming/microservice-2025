package cn.zm.ddd.shared.valueobject;

import java.util.UUID;

/**
 * 通用标识符值对象
 * 
 * DDD中的实体标识符，特点：
 * 1. 类型安全 - 避免不同实体ID混用
 * 2. 不可变性 - 一旦创建不能修改
 * 3. 自生成 - 提供生成新ID的方法
 * 4. 验证 - 确保ID的有效性
 */
public record EntityId(String value) {
    
    public EntityId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("实体ID不能为空");
        }
        
        // 验证UUID格式
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("实体ID格式不正确: " + value);
        }
    }
    
    /**
     * 生成新的实体ID
     */
    public static EntityId generate() {
        return new EntityId(UUID.randomUUID().toString());
    }
    
    /**
     * 从字符串创建实体ID
     */
    public static EntityId of(String value) {
        return new EntityId(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}