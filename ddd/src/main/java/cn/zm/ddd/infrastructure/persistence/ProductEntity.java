package cn.zm.ddd.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.zm.ddd.domain.model.product.Product;
import cn.zm.ddd.domain.model.product.ProductId;
import cn.zm.ddd.shared.valueobject.Money;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

/**
 * 商品持久化实体 - MyBatis-Plus + Lombok版本
 */
@Data
@NoArgsConstructor
@TableName("products")
public class ProductEntity {

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @TableField("name")
    private String name;

    @TableField("description")
    private String description;

    @TableField("price")
    private BigDecimal price;

    @TableField("currency")
    private String currency;

    @TableField("stock_quantity")
    private Integer stockQuantity;

    @TableField("available")
    private Boolean available;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("last_modified_at")
    private LocalDateTime lastModifiedAt;

    public static ProductEntity from(Product product) {
        ProductEntity entity = new ProductEntity();
        entity.id = product.getId().value();
        entity.name = product.getName();
        entity.description = product.getDescription();
        entity.price = product.getPrice().amount();
        entity.currency = product.getPrice().currency().getCurrencyCode();
        entity.stockQuantity = product.getStockQuantity();
        entity.available = product.isAvailable();
        entity.createdAt = product.getCreatedAt();
        entity.lastModifiedAt = product.getLastModifiedAt();
        return entity;
    }

    public Product toDomain() {
        Money money = new Money(this.price, Currency.getInstance(this.currency));

        return Product.restore(
                ProductId.of(this.id),
                this.name,
                this.description,
                money,
                this.stockQuantity,
                this.available,
                this.createdAt,
                this.lastModifiedAt
        );
    }

    public void updateFrom(Product product) {
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice().amount();
        this.currency = product.getPrice().currency().getCurrencyCode();
        this.stockQuantity = product.getStockQuantity();
        this.available = product.isAvailable();
        this.lastModifiedAt = product.getLastModifiedAt();
    }
}