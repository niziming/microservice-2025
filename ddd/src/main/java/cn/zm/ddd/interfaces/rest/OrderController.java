package cn.zm.ddd.interfaces.rest;

import cn.zm.ddd.application.command.AddProductToOrderCommand;
import cn.zm.ddd.application.command.CreateOrderCommand;
import cn.zm.ddd.application.dto.OrderDto;
import cn.zm.ddd.application.query.OrderQuery;
import cn.zm.ddd.application.service.OrderApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 订单REST控制器
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final OrderApplicationService orderApplicationService;
    
    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = Objects.requireNonNull(
            orderApplicationService, "订单应用服务不能为空");
    }
    
    /**
     * 创建订单
     */
    @PostMapping
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(
            @RequestBody CreateOrderRequest request) {
        
        CreateOrderCommand command = new CreateOrderCommand(request.customerId());
        OrderDto orderDto = orderApplicationService.createOrder(command);
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("订单创建成功", orderDto));
    }
    
    /**
     * 查询订单
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrder(
            @PathVariable String orderId) {
        
        Optional<OrderDto> order = orderApplicationService
            .findOrder(OrderQuery.byId(orderId));
        
        if (order.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("查询成功", order.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("订单不存在"));
        }
    }
    
    /**
     * 查询客户订单
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getCustomerOrders(
            @PathVariable String customerId) {
        
        List<OrderDto> orders = orderApplicationService.findCustomerOrders(customerId);
        
        return ResponseEntity.ok(ApiResponse.success("查询成功", orders));
    }
    
    /**
     * 添加商品到订单
     */
    @PostMapping("/{orderId}/items")
    public ResponseEntity<ApiResponse<OrderDto>> addProductToOrder(
            @PathVariable String orderId,
            @RequestBody AddProductToOrderRequest request) {
        
        AddProductToOrderCommand command = new AddProductToOrderCommand(
            orderId, request.productId(), request.quantity());
        
        OrderDto orderDto = orderApplicationService.addProductToOrder(command);
        
        return ResponseEntity.ok(ApiResponse.success("商品添加成功", orderDto));
    }
    
    /**
     * 支付订单
     */
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<ApiResponse<OrderDto>> payOrder(
            @PathVariable String orderId) {
        
        OrderDto orderDto = orderApplicationService.payOrder(orderId);
        
        return ResponseEntity.ok(ApiResponse.success("订单支付成功", orderDto));
    }
    
    /**
     * 发货订单
     */
    @PostMapping("/{orderId}/ship")
    public ResponseEntity<ApiResponse<OrderDto>> shipOrder(
            @PathVariable String orderId) {
        
        OrderDto orderDto = orderApplicationService.shipOrder(orderId);
        
        return ResponseEntity.ok(ApiResponse.success("订单已发货", orderDto));
    }
    
    /**
     * 确认收货
     */
    @PostMapping("/{orderId}/deliver")
    public ResponseEntity<ApiResponse<OrderDto>> deliverOrder(
            @PathVariable String orderId) {
        
        OrderDto orderDto = orderApplicationService.deliverOrder(orderId);
        
        return ResponseEntity.ok(ApiResponse.success("订单已送达", orderDto));
    }
    
    /**
     * 取消订单
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderDto>> cancelOrder(
            @PathVariable String orderId) {
        
        OrderDto orderDto = orderApplicationService.cancelOrder(orderId);
        
        return ResponseEntity.ok(ApiResponse.success("订单已取消", orderDto));
    }
}

/**
 * 创建订单请求
 */
record CreateOrderRequest(
    String customerId
) {}

/**
 * 添加商品到订单请求
 */
record AddProductToOrderRequest(
    String productId,
    int quantity
) {}