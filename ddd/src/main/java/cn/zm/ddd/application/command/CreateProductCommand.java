package cn.zm.ddd.application.command;

import java.math.BigDecimal;

/**
 * 创建商品命令
 */
public record CreateProductCommand(
    String name,
    String description,
    BigDecimal price,
    String currency,
    int stockQuantity
) {
    
    public CreateProductCommand {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("商品名称不能为空");
        }
        
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("商品价格不能为空或负数");
        }
        
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("货币类型不能为空");
        }
        
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("库存数量不能为负数");
        }
    }
    
    /**
     * 获取货币对象
     */
    public java.util.Currency getCurrencyObject() {
        return java.util.Currency.getInstance(currency);
    }
}