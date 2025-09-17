package cn.zm.ddd.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.zm.ddd.domain.model.order.OrderItem;
import cn.zm.ddd.domain.model.product.ProductId;
import cn.zm.ddd.shared.valueobject.Money;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * 订单项持久化实体 - MyBatis-Plus + Lombok版本
 */
@Data
@NoArgsConstructor
@TableName("order_items")
public class OrderItemEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("order_id")
    private String orderId;

    @TableField("product_id")
    private String productId;

    @TableField("product_name")
    private String productName;

    @TableField("unit_price")
    private BigDecimal unitPrice;

    @TableField("currency")
    private String currency;

    @TableField("quantity")
    private Integer quantity;

    public static OrderItemEntity from(OrderItem orderItem, String orderId) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.orderId = orderId;
        entity.productId = orderItem.productId().value();
        entity.productName = orderItem.productName();
        entity.unitPrice = orderItem.unitPrice().amount();
        entity.currency = orderItem.unitPrice().currency().getCurrencyCode();
        entity.quantity = orderItem.quantity();
        return entity;
    }

    public OrderItem toDomain() {
        Money money = new Money(this.unitPrice, Currency.getInstance(this.currency));

        return new OrderItem(
                ProductId.of(this.productId),
                this.productName,
                money,
                this.quantity
        );
    }
}