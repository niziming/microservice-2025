package cn.zm.ddd.application.service;

import cn.zm.ddd.application.command.AddProductToOrderCommand;
import cn.zm.ddd.application.command.CreateOrderCommand;
import cn.zm.ddd.application.dto.OrderDto;
import cn.zm.ddd.application.query.OrderQuery;
import cn.zm.ddd.domain.model.customer.Customer;
import cn.zm.ddd.domain.model.customer.CustomerId;
import cn.zm.ddd.domain.model.order.Order;
import cn.zm.ddd.domain.model.order.OrderId;
import cn.zm.ddd.domain.model.product.ProductId;
import cn.zm.ddd.domain.repository.CustomerRepository;
import cn.zm.ddd.domain.repository.OrderRepository;
import cn.zm.ddd.domain.service.OrderDomainService;
import cn.zm.ddd.shared.exception.BusinessRuleException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 订单应用服务
 * 
 * 展示了复杂业务场景下应用服务的协调作用：
 * 1. 协调多个聚合（订单、客户、商品）
 * 2. 使用领域服务处理跨聚合业务逻辑
 * 3. 管理事务边界
 */
@Service
@Transactional
public class OrderApplicationService {
    
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderDomainService orderDomainService;
    
    public OrderApplicationService(OrderRepository orderRepository,
                                 CustomerRepository customerRepository,
                                 OrderDomainService orderDomainService) {
        this.orderRepository = Objects.requireNonNull(orderRepository, "订单仓储不能为空");
        this.customerRepository = Objects.requireNonNull(customerRepository, "客户仓储不能为空");
        this.orderDomainService = Objects.requireNonNull(orderDomainService, "订单领域服务不能为空");
    }
    
    /**
     * 创建订单
     */
    public OrderDto createOrder(CreateOrderCommand command) {
        // 验证客户是否存在
        Customer customer = customerRepository.findById(CustomerId.of(command.customerId()))
            .orElseThrow(() -> new BusinessRuleException("客户不存在: " + command.customerId()));
        
        // 验证客户是否处于活跃状态
        if (!customer.isActive()) {
            throw new BusinessRuleException("无法为已停用客户创建订单");
        }
        
        // 创建订单
        Order order = Order.create(customer.getId());
        
        // 保存订单
        orderRepository.save(order);
        
        return OrderDto.from(order);
    }
    
    /**
     * 添加商品到订单
     * 
     * 体现了应用服务协调领域服务的作用
     */
    public OrderDto addProductToOrder(AddProductToOrderCommand command) {
        // 查找订单
        Order order = orderRepository.findById(OrderId.of(command.orderId()))
            .orElseThrow(() -> new BusinessRuleException("订单不存在: " + command.orderId()));
        
        // 使用领域服务添加商品（处理跨聚合的业务逻辑）
        orderDomainService.addProductToOrder(
            order,
            ProductId.of(command.productId()),
            command.quantity()
        );
        
        // 保存订单变更
        orderRepository.save(order);
        
        return OrderDto.from(order);
    }
    
    /**
     * 支付订单
     */
    public OrderDto payOrder(String orderId) {
        Order order = orderRepository.findById(OrderId.of(orderId))
            .orElseThrow(() -> new BusinessRuleException("订单不存在: " + orderId));
        
        // 使用领域服务验证订单支付条件
        orderDomainService.validateOrderForPayment(order);
        
        // 查找客户以应用折扣
        Customer customer = customerRepository.findById(order.getCustomerId())
            .orElseThrow(() -> new BusinessRuleException("客户不存在: " + order.getCustomerId()));
        
        // 应用客户折扣
        BigDecimal discountRate = orderDomainService.calculateCustomerDiscount(customer, order);
        if (discountRate.compareTo(BigDecimal.ZERO) > 0) {
            order.applyDiscount(discountRate);
        }
        
        // 支付订单
        order.pay();
        
        // 保存订单变更
        orderRepository.save(order);
        
        return OrderDto.from(order);
    }
    
    /**
     * 查询订单
     */
    @Transactional(readOnly = true)
    public Optional<OrderDto> findOrder(OrderQuery query) {
        Optional<Order> order = Optional.empty();
        
        if (query.orderId() != null) {
            order = orderRepository.findById(OrderId.of(query.orderId()));
        }
        
        return order.map(OrderDto::from);
    }
    
    /**
     * 查询客户订单
     */
    @Transactional(readOnly = true)
    public List<OrderDto> findCustomerOrders(String customerId) {
        return orderRepository.findByCustomerId(CustomerId.of(customerId)).stream()
            .map(OrderDto::from)
            .toList();
    }
    
    /**
     * 发货订单
     */
    public OrderDto shipOrder(String orderId) {
        Order order = orderRepository.findById(OrderId.of(orderId))
            .orElseThrow(() -> new BusinessRuleException("订单不存在: " + orderId));
        
        order.ship();
        orderRepository.save(order);
        
        return OrderDto.from(order);
    }
    
    /**
     * 确认收货
     */
    public OrderDto deliverOrder(String orderId) {
        Order order = orderRepository.findById(OrderId.of(orderId))
            .orElseThrow(() -> new BusinessRuleException("订单不存在: " + orderId));
        
        order.deliver();
        orderRepository.save(order);
        
        return OrderDto.from(order);
    }
    
    /**
     * 取消订单
     */
    public OrderDto cancelOrder(String orderId) {
        Order order = orderRepository.findById(OrderId.of(orderId))
            .orElseThrow(() -> new BusinessRuleException("订单不存在: " + orderId));
        
        order.cancel();
        orderRepository.save(order);
        
        return OrderDto.from(order);
    }
}