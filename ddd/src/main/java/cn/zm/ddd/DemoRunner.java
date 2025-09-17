package cn.zm.ddd;

import cn.zm.ddd.application.command.CreateCustomerCommand;
import cn.zm.ddd.application.command.CreateOrderCommand;
import cn.zm.ddd.application.command.CreateProductCommand;
import cn.zm.ddd.application.command.AddProductToOrderCommand;
import cn.zm.ddd.application.service.CustomerApplicationService;
import cn.zm.ddd.application.service.OrderApplicationService;
import cn.zm.ddd.application.service.ProductApplicationService;
import cn.zm.ddd.application.dto.CustomerDto;
import cn.zm.ddd.application.dto.ProductDto;
import cn.zm.ddd.application.dto.OrderDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * DDD演示脚本
 * 
 * 演示DDD架构的使用方式和业务流程
 */
@Component
public class DemoRunner implements CommandLineRunner {
    
    private final CustomerApplicationService customerService;
    private final ProductApplicationService productService;
    private final OrderApplicationService orderService;
    
    public DemoRunner(CustomerApplicationService customerService,
                     ProductApplicationService productService,
                     OrderApplicationService orderService) {
        this.customerService = customerService;
        this.productService = productService;
        this.orderService = orderService;
    }
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n=== DDD电商系统演示 ===\n");
        
        try {
            // 1. 创建客户
            System.out.println("1. 创建客户...");
            CreateCustomerCommand createCustomerCmd = new CreateCustomerCommand(
                "演示客户", "demo@example.com", "VIP"
            );
            CustomerDto customer = customerService.createCustomer(createCustomerCmd);
            System.out.println("客户创建成功: " + customer.getName() + " (" + customer.getCustomerTypeDescription() + ")");
            
            // 2. 创建商品
            System.out.println("\n2. 创建商品...");
            CreateProductCommand createProductCmd = new CreateProductCommand(
                "演示商品", "这是一个演示商品", new BigDecimal("299.99"), "CNY", 50
            );
            ProductDto product = productService.createProduct(createProductCmd);
            System.out.println("商品创建成功: " + product.name() + " - ¥" + product.price());
            
            // 3. 创建订单
            System.out.println("\n3. 创建订单...");
            CreateOrderCommand createOrderCmd = new CreateOrderCommand(customer.getId());
            OrderDto order = orderService.createOrder(createOrderCmd);
            System.out.println("订单创建成功: " + order.id());
            
            // 4. 添加商品到订单
            System.out.println("\n4. 添加商品到订单...");
            AddProductToOrderCommand addProductCmd = new AddProductToOrderCommand(
                order.id(), product.id(), 2
            );
            OrderDto updatedOrder = orderService.addProductToOrder(addProductCmd);
            System.out.println("商品添加成功，订单总金额: ¥" + updatedOrder.totalAmount());
            
            // 5. 支付订单
            System.out.println("\n5. 支付订单...");
            OrderDto paidOrder = orderService.payOrder(order.id());
            System.out.println("订单支付成功，状态: " + paidOrder.statusDescription());
            
            // 6. 升级客户
            System.out.println("\n6. 客户升级演示...");
            if (customer.getCustomerType().equals("REGULAR")) {
                CustomerDto upgradedCustomer = customerService.upgradeToVip(customer.getId());
                System.out.println("客户升级成功: " + upgradedCustomer.getCustomerTypeDescription());
            } else {
                System.out.println("客户已经是VIP，享受折扣: " + customer.isCanReceiveDiscount());
            }
            
            System.out.println("\n=== 演示完成 ===");
            System.out.println("这个演示展示了DDD架构的以下特点：");
            System.out.println("1. 聚合根封装业务逻辑");
            System.out.println("2. 应用服务协调多个聚合");
            System.out.println("3. 领域服务处理跨聚合逻辑");
            System.out.println("4. 值对象保证类型安全");
            System.out.println("5. 业务规则在领域层集中管理");
            
        } catch (Exception e) {
            System.err.println("演示过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}