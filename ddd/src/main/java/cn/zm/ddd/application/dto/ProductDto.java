package cn.zm.ddd.application.dto;

import cn.zm.ddd.domain.model.product.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品数据传输对象
 */

public record ProductDto(
    String id,
    String name,
    String description,
    BigDecimal price,
    String currency,
    int stockQuantity,
    boolean available,
    boolean outOfStock,
    LocalDateTime createdAt,
    LocalDateTime lastModifiedAt
) {
    
    /**
     * 从领域对象转换为DTO
     */
    public static ProductDto from(Product product) {
        return new ProductDto(
            product.getId().value(),
            product.getName(),
            product.getDescription(),
            product.getPrice().amount(),
            product.getPrice().currency().getCurrencyCode(),
            product.getStockQuantity(),
            product.isAvailable(),
            product.isOutOfStock(),
            product.getCreatedAt(),
            product.getLastModifiedAt()
        );
    }
}