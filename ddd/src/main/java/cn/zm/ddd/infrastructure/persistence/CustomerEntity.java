package cn.zm.ddd.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.zm.ddd.domain.model.customer.Customer;
import cn.zm.ddd.domain.model.customer.CustomerId;
import cn.zm.ddd.domain.model.customer.CustomerType;
import cn.zm.ddd.shared.valueobject.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 客户持久化实体 - MyBatis-Plus + Lombok版本
 * 
 * 使用Lombok的最佳实践：
 * 1. @Data - 自动生成getter/setter/equals/hashCode/toString
 * 2. @NoArgsConstructor - MyBatis-Plus需要无参构造函数
 * 3. 持久化实体是纯数据载体，适合使用Lombok简化
 */
@Data
@NoArgsConstructor
@TableName("customers")
public class CustomerEntity {
    
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    
    @TableField("name")
    private String name;
    
    @TableField("email")
    private String email;
    
    @TableField("customer_type")
    private String customerType;
    
    @TableField("active")
    private Boolean active;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("last_modified_at")
    private LocalDateTime lastModifiedAt;
    
    /**
     * 从领域对象创建实体
     */
    public static CustomerEntity from(Customer customer) {
        CustomerEntity entity = new CustomerEntity();
        entity.id = customer.getId().value();
        entity.name = customer.getName();
        entity.email = customer.getEmail().value();
        entity.customerType = customer.getType().name();
        entity.active = customer.isActive();
        entity.createdAt = customer.getCreatedAt();
        entity.lastModifiedAt = customer.getLastModifiedAt();
        return entity;
    }
    
    /**
     * 转换为领域对象
     */
    public Customer toDomain() {
        return Customer.restore(
            CustomerId.of(this.id),
            this.name,
            new Email(this.email),
            CustomerType.valueOf(this.customerType),
            this.active,
            this.createdAt,
            this.lastModifiedAt
        );
    }
    
    /**
     * 更新实体数据
     */
    public void updateFrom(Customer customer) {
        this.name = customer.getName();
        this.email = customer.getEmail().value();
        this.customerType = customer.getType().name();
        this.active = customer.isActive();
        this.lastModifiedAt = customer.getLastModifiedAt();
    }
}