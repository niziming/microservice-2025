package cn.zm.ddd.infrastructure.repository;

import cn.zm.ddd.domain.model.customer.CustomerId;
import cn.zm.ddd.domain.model.order.Order;
import cn.zm.ddd.domain.model.order.OrderId;
import cn.zm.ddd.domain.model.order.OrderStatus;
import cn.zm.ddd.domain.repository.OrderRepository;
import cn.zm.ddd.infrastructure.mapper.OrderMapper;
import cn.zm.ddd.infrastructure.mapper.OrderItemMapper;
import cn.zm.ddd.infrastructure.persistence.OrderEntity;
import cn.zm.ddd.infrastructure.persistence.OrderItemEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 订单仓储实现 - MyBatis-Plus版本
 * 
 * 由于订单和订单项的一对多关系，需要特殊处理：
 * 1. 保存时同时处理主表和从表
 * 2. 查询时需要加载关联数据
 * 3. 使用事务保证数据一致性
 */
@Repository
public class OrderRepositoryImpl implements OrderRepository {
    
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    
    public OrderRepositoryImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper) {
        this.orderMapper = Objects.requireNonNull(orderMapper, "订单Mapper不能为空");
        this.orderItemMapper = Objects.requireNonNull(orderItemMapper, "订单项Mapper不能为空");
    }
    
    @Override
    @Transactional
    public void save(Order order) {
        OrderEntity entity = OrderEntity.from(order);
        
        OrderEntity existingEntity = orderMapper.selectById(order.getId().value());
        
        if (existingEntity != null) {
            // 更新订单
            entity.updateFrom(order);
            orderMapper.updateById(entity);
            
            // 删除旧的订单项，重新插入
            orderItemMapper.deleteByOrderId(order.getId().value());
        } else {
            // 插入新订单
            orderMapper.insert(entity);
        }
        
        // 插入订单项
        for (var item : order.getItems()) {
            OrderItemEntity itemEntity = OrderItemEntity.from(item, order.getId().value());
            orderItemMapper.insert(itemEntity);
        }
    }
    
    @Override
    public Optional<Order> findById(OrderId orderId) {
        OrderEntity entity = orderMapper.selectById(orderId.value());
        if (entity == null) {
            return Optional.empty();
        }
        
        // 加载订单项
        List<OrderItemEntity> items = orderItemMapper.findByOrderId(orderId.value());
        entity.setItems(items);
        
        return Optional.of(entity.toDomain());
    }
    
    @Override
    public List<Order> findByCustomerId(CustomerId customerId) {
        List<OrderEntity> entities = orderMapper.findByCustomerId(customerId.value());
        
        // 为每个订单加载订单项
        return entities.stream().map(entity -> {
            List<OrderItemEntity> items = orderItemMapper.findByOrderId(entity.getId());
            entity.setItems(items);
            return entity.toDomain();
        }).toList();
    }
    
    @Override
    public List<Order> findByStatus(OrderStatus status) {
        List<OrderEntity> entities = orderMapper.findByStatus(status.name());
        
        return entities.stream().map(entity -> {
            List<OrderItemEntity> items = orderItemMapper.findByOrderId(entity.getId());
            entity.setItems(items);
            return entity.toDomain();
        }).toList();
    }
    
    @Override
    public List<Order> findByCustomerIdAndStatus(CustomerId customerId, OrderStatus status) {
        List<OrderEntity> entities = orderMapper.findByCustomerIdAndStatus(
            customerId.value(), status.name());
        
        return entities.stream().map(entity -> {
            List<OrderItemEntity> items = orderItemMapper.findByOrderId(entity.getId());
            entity.setItems(items);
            return entity.toDomain();
        }).toList();
    }
    
    @Override
    @Transactional
    public void deleteById(OrderId orderId) {
        // 先删除订单项，再删除订单
        orderItemMapper.deleteByOrderId(orderId.value());
        orderMapper.deleteById(orderId.value());
    }
}