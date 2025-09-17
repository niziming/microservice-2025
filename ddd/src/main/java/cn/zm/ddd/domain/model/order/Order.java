package cn.zm.ddd.domain.model.order;

import cn.zm.ddd.domain.model.customer.CustomerId;
import cn.zm.ddd.domain.model.product.ProductId;
import cn.zm.ddd.shared.valueobject.Money;
import cn.zm.ddd.shared.exception.BusinessRuleException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 订单聚合根
 * 
 * DDD聚合根的经典实现：
 * 1. 作为聚合的唯一入口点
 * 2. 维护聚合内部一致性
 * 3. 封装复杂的业务逻辑
 * 4. 管理聚合内的实体和值对象
 */
public class Order {
    private final OrderId id;
    private final CustomerId customerId;
    private final List<OrderItem> items;
    private OrderStatus status;
    private Money totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    
    /**
     * 私有构造函数 - 强制使用工厂方法
     */
    private Order(OrderId id, CustomerId customerId) {
        this.id = Objects.requireNonNull(id, "订单ID不能为空");
        this.customerId = Objects.requireNonNull(customerId, "客户ID不能为空");
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.totalAmount = Money.cny(BigDecimal.ZERO);
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 工厂方法 - 创建新订单
     */
    public static Order create(CustomerId customerId) {
        return new Order(OrderId.generate(), customerId);
    }
    
    /**
     * 工厂方法 - 从持久化数据重建订单
     */
    public static Order restore(OrderId id, CustomerId customerId, List<OrderItem> items,
                              OrderStatus status, Money totalAmount,
                              LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        Order order = new Order(id, customerId);
        order.items.addAll(items);
        order.status = status;
        order.totalAmount = totalAmount;
        order.createdAt = createdAt;
        order.lastModifiedAt = lastModifiedAt;
        return order;
    }
    
    /**
     * 添加订单项
     * 核心业务方法 - 包含业务规则验证
     */
    public void addItem(ProductId productId, String productName, Money unitPrice, int quantity) {
        if (this.status != OrderStatus.PENDING) {
            throw new BusinessRuleException("只能向待支付订单添加商品");
        }
        
        // 检查是否已存在相同商品，如果存在则合并数量
        boolean itemExists = false;
        List<OrderItem> updatedItems = new ArrayList<>();
        
        for (OrderItem existingItem : this.items) {
            if (existingItem.isSameProduct(productId)) {
                // 合并相同商品的数量
                int newQuantity = existingItem.quantity() + quantity;
                updatedItems.add(new OrderItem(productId, productName, unitPrice, newQuantity));
                itemExists = true;
            } else {
                updatedItems.add(existingItem);
            }
        }
        
        if (!itemExists) {
            updatedItems.add(new OrderItem(productId, productName, unitPrice, quantity));
        }
        
        // 更新订单项列表
        this.items.clear();
        this.items.addAll(updatedItems);
        
        // 重新计算总金额
        this.recalculateTotalAmount();
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 移除订单项
     */
    public void removeItem(ProductId productId) {
        if (this.status != OrderStatus.PENDING) {
            throw new BusinessRuleException("只能从待支付订单移除商品");
        }
        
        boolean removed = this.items.removeIf(item -> item.isSameProduct(productId));
        
        if (!removed) {
            throw new BusinessRuleException("订单中不存在指定商品");
        }
        
        this.recalculateTotalAmount();
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 支付订单
     * 状态转换方法
     */
    public void pay() {
        if (this.status != OrderStatus.PENDING) {
            throw new BusinessRuleException("只有待支付订单才能进行支付");
        }
        
        if (this.items.isEmpty()) {
            throw new BusinessRuleException("空订单无法支付");
        }
        
        this.status = OrderStatus.PAID;
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 发货
     */
    public void ship() {
        if (!this.status.canBeShipped()) {
            throw new BusinessRuleException("当前状态的订单无法发货");
        }
        
        this.status = OrderStatus.SHIPPED;
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 确认收货
     */
    public void deliver() {
        if (this.status != OrderStatus.SHIPPED) {
            throw new BusinessRuleException("只有已发货订单才能确认收货");
        }
        
        this.status = OrderStatus.DELIVERED;
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 取消订单
     */
    public void cancel() {
        if (!this.status.canBeCancelled()) {
            throw new BusinessRuleException("当前状态的订单无法取消");
        }
        
        this.status = OrderStatus.CANCELLED;
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 退款
     */
    public void refund() {
        if (!this.status.canBeRefunded()) {
            throw new BusinessRuleException("当前状态的订单无法退款");
        }
        
        this.status = OrderStatus.REFUNDED;
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 应用折扣
     * 业务逻辑方法
     */
    public void applyDiscount(BigDecimal discountRate) {
        if (this.status != OrderStatus.PENDING) {
            throw new BusinessRuleException("只能对待支付订单应用折扣");
        }
        
        if (discountRate.compareTo(BigDecimal.ZERO) < 0 || 
            discountRate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("折扣率必须在0-1之间");
        }
        
        Money discountAmount = this.totalAmount.multiply(discountRate);
        this.totalAmount = this.totalAmount.subtract(discountAmount);
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 重新计算总金额
     * 私有方法 - 维护聚合一致性
     */
    private void recalculateTotalAmount() {
        this.totalAmount = this.items.stream()
            .map(OrderItem::calculateSubtotal)
            .reduce(Money.cny(BigDecimal.ZERO), Money::add);
    }
    
    /**
     * 获取订单项数量
     */
    public int getItemCount() {
        return this.items.size();
    }
    
    /**
     * 获取商品总数量
     */
    public int getTotalQuantity() {
        return this.items.stream()
            .mapToInt(OrderItem::quantity)
            .sum();
    }
    
    /**
     * 检查是否包含指定商品
     */
    public boolean containsProduct(ProductId productId) {
        return this.items.stream()
            .anyMatch(item -> item.isSameProduct(productId));
    }
    
    // Getters
    public OrderId getId() {
        return id;
    }
    
    public CustomerId getCustomerId() {
        return customerId;
    }
    
    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public Money getTotalAmount() {
        return totalAmount;
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
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Order{id=%s, customerId=%s, status=%s, total=%s, items=%d}", 
                           id, customerId, status, totalAmount, items.size());
    }
}