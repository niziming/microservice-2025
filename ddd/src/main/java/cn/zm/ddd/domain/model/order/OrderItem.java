package cn.zm.ddd.domain.model.order;

import cn.zm.ddd.domain.model.product.ProductId;
import cn.zm.ddd.shared.valueobject.Money;

/**
 * 订单项值对象
 * 
 * DDD中的值对象，表示订单中的单个商品项
 * 特点：
 * 1. 不可变性 - 一旦创建不能修改
 * 2. 无标识性 - 通过内容识别
 * 3. 包含业务逻辑 - 计算小计金额
 */
public record OrderItem(
    ProductId productId,
    String productName,
    Money unitPrice,
    int quantity
) {
    
    public OrderItem {
        if (productId == null) {
            throw new IllegalArgumentException("商品ID不能为空");
        }
        
        if (productName == null || productName.trim().isEmpty()) {
            throw new IllegalArgumentException("商品名称不能为空");
        }
        
        if (unitPrice == null) {
            throw new IllegalArgumentException("单价不能为空");
        }
        
        if (quantity <= 0) {
            throw new IllegalArgumentException("数量必须大于0");
        }
    }
    
    /**
     * 计算订单项小计
     * 业务逻辑方法
     */
    public Money calculateSubtotal() {
        return unitPrice.multiply(java.math.BigDecimal.valueOf(quantity));
    }
    
    /**
     * 检查是否是同一商品
     */
    public boolean isSameProduct(ProductId otherProductId) {
        return this.productId.equals(otherProductId);
    }
    
    @Override
    public String toString() {
        return String.format("OrderItem{product=%s, name='%s', price=%s, qty=%d, subtotal=%s}", 
                           productId, productName, unitPrice, quantity, calculateSubtotal());
    }
}