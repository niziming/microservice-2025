package cn.zm.ddd.domain.repository;

import cn.zm.ddd.domain.model.order.Order;
import cn.zm.ddd.domain.model.order.OrderId;
import cn.zm.ddd.domain.model.order.OrderStatus;
import cn.zm.ddd.domain.model.customer.CustomerId;

import java.util.List;
import java.util.Optional;

/**
 * 订单仓储接口
 */
public interface OrderRepository {
    
    /**
     * 保存订单
     */
    void save(Order order);
    
    /**
     * 根据ID查找订单
     */
    Optional<Order> findById(OrderId orderId);
    
    /**
     * 查找客户的所有订单
     */
    List<Order> findByCustomerId(CustomerId customerId);
    
    /**
     * 根据状态查找订单
     */
    List<Order> findByStatus(OrderStatus status);
    
    /**
     * 查找客户的特定状态订单
     */
    List<Order> findByCustomerIdAndStatus(CustomerId customerId, OrderStatus status);
    
    /**
     * 删除订单
     */
    void deleteById(OrderId orderId);
}