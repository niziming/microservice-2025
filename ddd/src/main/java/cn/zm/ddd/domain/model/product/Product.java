package cn.zm.ddd.domain.model.product;

import cn.zm.ddd.shared.valueobject.Money;
import cn.zm.ddd.shared.exception.BusinessRuleException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 商品聚合根
 * 
 * 商品聚合包含商品的基本信息、库存管理等
 */
public class Product {
    private final ProductId id;
    private String name;
    private String description;
    private Money price;
    private int stockQuantity;
    private boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    
    /**
     * 私有构造函数
     */
    private Product(ProductId id, String name, String description, Money price, int stockQuantity) {
        this.id = Objects.requireNonNull(id, "商品ID不能为空");
        this.name = validateName(name);
        this.description = validateDescription(description);
        this.price = Objects.requireNonNull(price, "商品价格不能为空");
        this.stockQuantity = validateStockQuantity(stockQuantity);
        this.available = true;
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 工厂方法 - 创建新商品
     */
    public static Product create(String name, String description, Money price, int stockQuantity) {
        return new Product(ProductId.generate(), name, description, price, stockQuantity);
    }
    
    /**
     * 工厂方法 - 从持久化数据重建商品对象
     */
    public static Product restore(ProductId id, String name, String description, 
                                Money price, int stockQuantity, boolean available,
                                LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        Product product = new Product(id, name, description, price, stockQuantity);
        product.available = available;
        product.createdAt = createdAt;
        product.lastModifiedAt = lastModifiedAt;
        return product;
    }
    
    /**
     * 更新商品信息
     */
    public void updateInfo(String newName, String newDescription, Money newPrice) {
        if (!this.available) {
            throw new BusinessRuleException("无法更新已下架的商品信息");
        }
        
        this.name = validateName(newName);
        this.description = validateDescription(newDescription);
        this.price = Objects.requireNonNull(newPrice, "商品价格不能为空");
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 减少库存
     * 核心业务方法 - 处理库存扣减逻辑
     */
    public void reduceStock(int quantity) {
        if (!this.available) {
            throw new BusinessRuleException("已下架商品无法减少库存");
        }
        
        if (quantity <= 0) {
            throw new IllegalArgumentException("减少库存数量必须大于0");
        }
        
        if (this.stockQuantity < quantity) {
            throw new BusinessRuleException(
                String.format("库存不足，当前库存：%d，请求数量：%d", this.stockQuantity, quantity)
            );
        }
        
        this.stockQuantity -= quantity;
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 增加库存
     */
    public void increaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("增加库存数量必须大于0");
        }
        
        this.stockQuantity += quantity;
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 下架商品
     */
    public void takeOffShelf() {
        if (!this.available) {
            throw new BusinessRuleException("商品已经下架");
        }
        
        this.available = false;
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 上架商品
     */
    public void putOnShelf() {
        if (this.available) {
            throw new BusinessRuleException("商品已经上架");
        }
        
        if (this.stockQuantity <= 0) {
            throw new BusinessRuleException("无库存商品无法上架");
        }
        
        this.available = true;
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * 检查是否有足够库存
     */
    public boolean hasEnoughStock(int quantity) {
        return this.available && this.stockQuantity >= quantity;
    }
    
    /**
     * 检查是否缺货
     */
    public boolean isOutOfStock() {
        return this.stockQuantity <= 0;
    }
    
    /**
     * 验证商品名称
     */
    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("商品名称不能为空");
        }
        
        if (name.length() > 100) {
            throw new IllegalArgumentException("商品名称长度不能超过100个字符");
        }
        
        return name.trim();
    }
    
    /**
     * 验证商品描述
     */
    private String validateDescription(String description) {
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("商品描述长度不能超过500个字符");
        }
        
        return description != null ? description.trim() : "";
    }
    
    /**
     * 验证库存数量
     */
    private int validateStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("库存数量不能为负数");
        }
        
        return stockQuantity;
    }
    
    // Getters
    public ProductId getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Money getPrice() {
        return price;
    }
    
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    public boolean isAvailable() {
        return available;
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
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Product{id=%s, name='%s', price=%s, stock=%d, available=%s}", 
                           id, name, price, stockQuantity, available);
    }
}