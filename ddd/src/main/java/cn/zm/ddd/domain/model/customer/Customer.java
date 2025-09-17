package cn.zm.ddd.domain.model.customer;

import cn.zm.ddd.shared.valueobject.Email;
import cn.zm.ddd.shared.exception.BusinessRuleException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 客户聚合根
 * 
 * DDD聚合根的特点：
 * 1. 作为聚合的唯一入口点
 * 2. 维护聚合内部的一致性
 * 3. 封装业务逻辑和业务规则
 * 4. 通过方法而非直接访问字段来修改状态
 */
public class Customer {
    private final CustomerId id;
    private String name;
    private Email email;
    private CustomerType type;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    
    /**
     * 私有构造函数 - 强制使用工厂方法创建
     */
    private Customer(CustomerId id, String name, Email email, CustomerType type) {
        this.id = Objects.requireNonNull(id, "客户ID不能为空");
        this.name = validateName(name);
        this.email = Objects.requireNonNull(email, "邮箱不能为空");
        this.type = Objects.requireNonNull(type, "客户类型不能为空");
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 工厂方法 - 创建新客户
     * 体现了DDD中通过工厂方法保证对象创建的有效性
     */
    public static Customer create(String name, Email email, CustomerType type) {
        return new Customer(CustomerId.generate(), name, email, type);
    }
    
    /**
     * 工厂方法 - 从持久化数据重建客户对象
     */
    public static Customer restore(CustomerId id, String name, Email email, 
                                 CustomerType type, boolean active, 
                                 LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        Customer customer = new Customer(id, name, email, type);
        customer.active = active;
        customer.createdAt = createdAt;
        customer.lastModifiedAt = lastModifiedAt;
        return customer;
    }
    
    /**
     * 更新客户信息
     * 业务方法 - 封装了更新逻辑和验证
     */
    public void updateInfo(String newName, Email newEmail) {
        if (!this.active) {
            throw new BusinessRuleException("无法更新已停用的客户信息");
        }
        
        this.name = validateName(newName);
        this.email = Objects.requireNonNull(newEmail, "邮箱不能为空");
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 升级客户类型
     * 业务方法 - 包含业务规则验证
     */
    public void upgradeToVip() {
        if (!this.active) {
            throw new BusinessRuleException("无法升级已停用的客户");
        }
        
        if (this.type == CustomerType.VIP) {
            throw new BusinessRuleException("客户已经是VIP类型");
        }
        
        this.type = CustomerType.VIP;
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 停用客户
     * 业务方法
     */
    public void deactivate() {
        if (!this.active) {
            throw new BusinessRuleException("客户已经被停用");
        }
        
        this.active = false;
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 激活客户
     * 业务方法
     */
    public void activate() {
        if (this.active) {
            throw new BusinessRuleException("客户已经是激活状态");
        }
        
        this.active = true;
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 检查是否可以享受折扣
     * 业务查询方法
     */
    public boolean canReceiveDiscount() {
        return this.active && this.type.isEligibleForDiscount();
    }
    
    /**
     * 验证姓名的私有方法
     */
    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("客户姓名不能为空");
        }
        
        if (name.length() > 50) {
            throw new IllegalArgumentException("客户姓名长度不能超过50个字符");
        }
        
        return name.trim();
    }
    
    // Getters - 只读访问
    public CustomerId getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Email getEmail() {
        return email;
    }
    
    public CustomerType getType() {
        return type;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Customer{id=%s, name='%s', email=%s, type=%s, active=%s}", 
                           id, name, email, type, active);
    }
}