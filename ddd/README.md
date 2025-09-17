# DDD电商系统架构说明 (MyBatis-Plus版本)

## 项目概述

本项目是一个基于DDD（领域驱动设计）架构思想实现的电商系统示例，使用Java 21和Spring Boot 3.3.2构建，**持久层使用MyBatis-Plus**。项目完整展示了DDD的核心概念和架构模式，为学习和实践DDD提供了一个完整的参考实现。

## DDD架构层次

### 1. 领域层 (Domain Layer)

领域层是DDD的核心，包含了业务的核心逻辑和规则。

#### 实体 (Entities)
- **`Customer`** - 客户聚合根
  - 包含客户的基本信息和业务行为
  - 封装了客户升级、激活/停用等业务逻辑
  - 通过工厂方法创建，确保业务规则的一致性

- **`Product`** - 商品聚合根
  - 管理商品信息、库存、上下架状态
  - 封装了库存扣减、价格管理等核心业务逻辑

- **`Order`** - 订单聚合根
  - 最复杂的聚合，管理订单状态和订单项
  - 包含订单状态流转、折扣应用等复杂业务逻辑

#### 值对象 (Value Objects)
- **`Email`** - 邮箱地址值对象，包含邮箱格式验证
- **`Money`** - 金额值对象，处理货币计算和精度问题
- **`EntityId`** - 通用标识符值对象，确保类型安全
- **`OrderItem`** - 订单项值对象，不可变且包含计算逻辑

#### 领域服务 (Domain Services)
- **`OrderDomainService`** - 订单领域服务
  - 处理跨聚合的业务逻辑
  - 协调订单、商品、客户之间的交互
  - 实现复杂的业务规则验证

#### 仓储接口 (Repository Interfaces)
- 定义了数据访问的抽象接口
- 使用领域对象作为参数和返回值
- 屏蔽了基础设施层的技术细节

### 2. 应用层 (Application Layer)

应用层协调领域对象完成用户用例，管理事务边界。

#### 应用服务 (Application Services)
- **`CustomerApplicationService`** - 客户应用服务
- **`ProductApplicationService`** - 商品应用服务
- **`OrderApplicationService`** - 订单应用服务

每个应用服务都：
- 实现具体的用户用例
- 管理事务边界
- 协调多个聚合的操作
- 处理跨聚合的业务场景

#### 命令 (Commands) - CQRS模式
- **`CreateCustomerCommand`** - 创建客户命令
- **`CreateProductCommand`** - 创建商品命令
- **`AddProductToOrderCommand`** - 添加商品到订单命令

#### 查询 (Queries) - CQRS模式
- **`CustomerQuery`** - 客户查询对象
- **`OrderQuery`** - 订单查询对象

#### 数据传输对象 (DTOs)
- **`CustomerDto`** - 客户数据传输对象
- **`ProductDto`** - 商品数据传输对象
- **`OrderDto`** - 订单数据传输对象
- **`OrderItemDto`** - 订单项数据传输对象

### 3. 基础设施层 (Infrastructure Layer)

基础设施层提供技术能力，支撑上层业务逻辑的实现。

#### 持久化实体 (Persistence Entities)
- **`CustomerEntity`** - 客户持久化实体（MyBatis-Plus）
- **`ProductEntity`** - 商品持久化实体（MyBatis-Plus）
- **`OrderEntity`** - 订单持久化实体（MyBatis-Plus）
- **`OrderItemEntity`** - 订单项持久化实体（MyBatis-Plus）

#### 仓储实现 (Repository Implementations)
- **`CustomerRepositoryImpl`** - 客户仓储实现（MyBatis-Plus）
- **`ProductRepositoryImpl`** - 商品仓储实现（MyBatis-Plus）
- **`OrderRepositoryImpl`** - 订单仓储实现（MyBatis-Plus）

#### MyBatis-Plus Mapper接口
- **`CustomerMapper`** - 客户Mapper接口
- **`ProductMapper`** - 商品Mapper接口
- **`OrderMapper`** - 订单Mapper接口
- **`OrderItemMapper`** - 订单项Mapper接口

### 4. 用户接口层 (Interface Layer)

用户接口层处理用户交互，将外部请求转换为应用层调用。

#### REST控制器
- **`CustomerController`** - 客户REST API
- **`ProductController`** - 商品REST API
- **`OrderController`** - 订单REST API

#### 统一响应格式
- **`ApiResponse`** - 统一API响应格式
- **`GlobalExceptionHandler`** - 全局异常处理

## DDD核心概念的体现

### 1. 聚合 (Aggregates)

**为什么需要聚合？**
- 确保业务不变性和一致性
- 定义事务边界
- 控制并发访问

**本项目中的聚合：**
- **客户聚合** - 以Customer为聚合根，管理客户相关的所有业务规则
- **商品聚合** - 以Product为聚合根，管理商品和库存
- **订单聚合** - 以Order为聚合根，包含OrderItem值对象

### 2. 聚合根 (Aggregate Roots)

**特点：**
- 作为聚合的唯一入口点
- 维护聚合内部的一致性
- 通过ID被外部引用

**实现：**
```java
// 订单聚合根示例
public class Order {
    private final OrderId id;  // 聚合根标识
    private final List<OrderItem> items;  // 聚合内的实体集合
    
    // 业务方法维护一致性
    public void addItem(ProductId productId, String productName, Money unitPrice, int quantity) {
        // 业务规则验证
        // 状态一致性维护
    }
}
```

### 3. 值对象 (Value Objects)

**为什么使用值对象？**
- 类型安全 - 避免原始类型滥用
- 不可变性 - 确保数据完整性
- 业务表达力 - 封装业务规则和验证

**实现示例：**
```java
// 金额值对象
public record Money(BigDecimal amount, Currency currency) {
    public Money {
        // 构造时验证
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("金额不能为负数");
        }
    }
    
    // 业务行为
    public Money add(Money other) {
        // 业务逻辑实现
    }
}
```

### 4. 领域服务 (Domain Services)

**使用场景：**
- 跨聚合的业务逻辑
- 不属于任何单个实体的业务规则
- 复杂的业务计算

**实现示例：**
```java
public class OrderDomainService {
    // 跨聚合业务逻辑：订单+商品+客户
    public void addProductToOrder(Order order, ProductId productId, int quantity) {
        // 验证商品
        // 检查库存
        // 更新订单
        // 扣减库存
    }
}
```

### 5. 仓储模式 (Repository Pattern)

**价值：**
- 抽象数据访问
- 使用领域语言定义查询
- 支持单元测试（mock仓储）

**实现示例：**
```java
// 领域层 - 接口定义
public interface CustomerRepository {
    void save(Customer customer);
    Optional<Customer> findById(CustomerId customerId);
}

// 基础设施层 - MyBatis-Plus实现
@Repository
public class CustomerRepositoryImpl implements CustomerRepository {
    private final CustomerMapper customerMapper;
    // MyBatis-Plus Mapper实现
}
```

### 6. CQRS模式 (Command Query Responsibility Segregation)

**实现：**
- **命令** - 表示修改操作的意图
- **查询** - 表示查询操作的条件
- **分离关注点** - 读写操作分别优化

### 7. 工厂模式

**使用场景：**
- 复杂对象创建
- 确保业务规则
- 隐藏创建复杂性

**实现：**
```java
public class Customer {
    // 工厂方法
    public static Customer create(String name, Email email, CustomerType type) {
        // 验证规则
        // 创建对象
        return new Customer(CustomerId.generate(), name, email, type);
    }
}
```

## 项目架构优势

### 1. 业务逻辑清晰
- 业务规则集中在领域对象中
- 代码结构反映业务模型
- 易于理解和维护

### 2. 高度解耦
- 各层职责明确
- 依赖方向合理（向内依赖）
- 易于替换基础设施

### 3. 可测试性强
- 业务逻辑可独立测试
- 仓储接口可mock
- 单元测试覆盖面广

### 4. 可扩展性好
- 新增业务功能影响面小
- 可独立演化各个聚合
- 支持微服务拆分

## 运行说明

### 1. 环境要求
- Java 21+
- Maven 3.8+

### 2. 启动应用
```bash
mvn clean compile
mvn spring-boot:run
```

### 3. 访问地址
- 应用: http://localhost:8080
- H2控制台: http://localhost:8080/h2-console

### 4. API示例

#### 创建客户
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "测试用户",
    "email": "test@example.com",
    "customerType": "REGULAR"
  }'
```

#### 创建商品
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "测试商品",
    "description": "这是一个测试商品",
    "price": 99.99,
    "currency": "CNY",
    "stockQuantity": 100
  }'
```

#### 创建订单
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "客户ID"
  }'
```

## 总结

本项目完整展示了DDD架构的实现方式，包含了：

1. **战略设计** - 识别聚合边界和上下文
2. **战术设计** - 实体、值对象、聚合、仓储等模式
3. **架构分层** - 清晰的分层架构和依赖关系
4. **工程实践** - Spring Boot集成和最佳实践

通过这个示例，可以深入理解DDD的核心思想和实现方法，为实际项目应用DDD提供参考。