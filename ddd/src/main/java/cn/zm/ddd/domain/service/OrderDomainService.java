package cn.zm.ddd.domain.service;

import cn.zm.ddd.domain.model.customer.CustomerType;
import cn.zm.ddd.domain.model.customer.Customer;
import cn.zm.ddd.domain.model.order.Order;
import cn.zm.ddd.domain.model.product.Product;
import cn.zm.ddd.domain.model.product.ProductId;
import cn.zm.ddd.domain.repository.ProductRepository;
import cn.zm.ddd.shared.exception.BusinessRuleException;

import java.util.Objects;

/**
 * 订单领域服务
 * 
 * DDD领域服务的特点：
 * 1. 包含不属于单个聚合的业务逻辑
 * 2. 协调多个聚合之间的交互
 * 3. 实现复杂的业务规则和策略
 * 4. 无状态的服务对象
 */
public class OrderDomainService {
    
    private final ProductRepository productRepository;
    
    public OrderDomainService(ProductRepository productRepository) {
        this.productRepository = Objects.requireNonNull(productRepository, "商品仓储不能为空");
    }
    
    /**
     * 验证并添加商品到订单
     * 
     * 这个方法体现了领域服务的价值：
     * 1. 跨聚合的业务逻辑（Order和Product）
     * 2. 复杂的业务规则验证
     * 3. 协调多个仓储的操作
     */
    public void addProductToOrder(Order order, ProductId productId, int quantity) {
        // 验证商品是否存在
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new BusinessRuleException("商品不存在: " + productId));
        
        // 验证商品是否可售
        if (!product.isAvailable()) {
            throw new BusinessRuleException("商品已下架，无法添加到订单: " + product.getName());
        }
        
        // 验证库存是否充足
        if (!product.hasEnoughStock(quantity)) {
            throw new BusinessRuleException(
                String.format("商品库存不足，商品：%s，当前库存：%d，请求数量：%d", 
                             product.getName(), product.getStockQuantity(), quantity));
        }
        
        // 添加商品到订单
        order.addItem(productId, product.getName(), product.getPrice(), quantity);
        
        // 减少商品库存
        product.reduceStock(quantity);
        
        // 保存商品库存变更
        productRepository.save(product);
    }
    
    /**
     * 计算客户折扣
     * 
     * 根据客户类型和订单金额计算折扣率
     */
    public java.math.BigDecimal calculateCustomerDiscount(Customer customer, Order order) {
        if (!customer.canReceiveDiscount()) {
            return java.math.BigDecimal.ZERO;
        }
        
        // VIP客户享受5%折扣
        if (customer.getType() == CustomerType.VIP) {
            return new java.math.BigDecimal("0.05");
        }
        
        // 企业客户享受10%折扣
        if (customer.getType() == CustomerType.ENTERPRISE) {
            return new java.math.BigDecimal("0.10");
        }
        
        return java.math.BigDecimal.ZERO;
    }
    
    /**
     * 验证订单是否可以支付
     */
    public void validateOrderForPayment(Order order) {
        if (order.getItems().isEmpty()) {
            throw new BusinessRuleException("空订单无法支付");
        }
        
        if (order.getTotalAmount().isZero()) {
            throw new BusinessRuleException("订单金额为零无法支付");
        }
        
        // 验证所有商品仍然可用且有库存
        for (var item : order.getItems()) {
            Product product = productRepository.findById(item.productId())
                .orElseThrow(() -> new BusinessRuleException("订单中的商品不存在: " + item.productId()));
            
            if (!product.isAvailable()) {
                throw new BusinessRuleException("订单中包含已下架商品: " + product.getName());
            }
        }
    }
}