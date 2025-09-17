package cn.zm.ddd.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.zm.ddd.domain.model.customer.CustomerId;
import cn.zm.ddd.domain.model.order.Order;
import cn.zm.ddd.domain.model.order.OrderId;
import cn.zm.ddd.domain.model.order.OrderItem;
import cn.zm.ddd.domain.model.order.OrderStatus;
import cn.zm.ddd.shared.valueobject.Money;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

/**
 * 订单持久化实体 - MyBatis-Plus + Lombok版本
 */
@Data
@NoArgsConstructor
@TableName("orders")
public class OrderEntity {
    
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    
    @TableField("customer_id")
    private String customerId;
    
    @TableField("status")
    private String status;
    
    @TableField("total_amount")
    private BigDecimal totalAmount;
    
    @TableField("currency")
    private String currency;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("last_modified_at")
    private LocalDateTime lastModifiedAt;
    
    /**
     * 订单项列表 - 通过关联查询获取
     * 不存储在数据库中，用于对象关联
     */
    @TableField(exist = false)
    private List<OrderItemEntity> items;
    
    public static OrderEntity from(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.id = order.getId().value();
        entity.customerId = order.getCustomerId().value();
        entity.status = order.getStatus().name();
        entity.totalAmount = order.getTotalAmount().amount();
        entity.currency = order.getTotalAmount().currency().getCurrencyCode();
        entity.createdAt = order.getCreatedAt();
        entity.lastModifiedAt = order.getLastModifiedAt();
        return entity;
    }
    
    public Order toDomain() {
        Money money = new Money(this.totalAmount, Currency.getInstance(this.currency));
        
        List<OrderItem> domainItems = this.items != null ? 
            this.items.stream().map(OrderItemEntity::toDomain).toList() :
            List.of();
        
        return Order.restore(
            OrderId.of(this.id),
            CustomerId.of(this.customerId),
            domainItems,
            OrderStatus.valueOf(this.status),
            money,
            this.createdAt,
            this.lastModifiedAt
        );
    }
    
    public void updateFrom(Order order) {
        this.status = order.getStatus().name();
        this.totalAmount = order.getTotalAmount().amount();
        this.currency = order.getTotalAmount().currency().getCurrencyCode();
        this.lastModifiedAt = order.getLastModifiedAt();
    }
}