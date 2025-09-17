package cn.zm.ddd.application.service;

import cn.zm.ddd.application.command.CreateProductCommand;
import cn.zm.ddd.application.dto.ProductDto;
import cn.zm.ddd.domain.model.product.Product;
import cn.zm.ddd.domain.model.product.ProductId;
import cn.zm.ddd.domain.repository.ProductRepository;
import cn.zm.ddd.shared.exception.BusinessRuleException;
import cn.zm.ddd.shared.valueobject.Money;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 商品应用服务
 */
@Service
@Transactional
public class ProductApplicationService {
    
    private final ProductRepository productRepository;
    
    public ProductApplicationService(ProductRepository productRepository) {
        this.productRepository = Objects.requireNonNull(productRepository, "商品仓储不能为空");
    }
    
    /**
     * 创建商品
     */
    public ProductDto createProduct(CreateProductCommand command) {
        Money price = new Money(command.price(), command.getCurrencyObject());
        
        Product product = Product.create(
            command.name(),
            command.description(),
            price,
            command.stockQuantity()
        );
        
        productRepository.save(product);
        
        return ProductDto.from(product);
    }
    
    /**
     * 查找商品
     */
    @Transactional(readOnly = true)
    public Optional<ProductDto> findProduct(String productId) {
        return productRepository.findById(ProductId.of(productId))
            .map(ProductDto::from);
    }
    
    /**
     * 查找所有可用商品
     */
    @Transactional(readOnly = true)
    public List<ProductDto> findAllAvailableProducts() {
        return productRepository.findAllAvailable().stream()
            .map(ProductDto::from)
            .toList();
    }
    
    /**
     * 搜索商品
     */
    @Transactional(readOnly = true)
    public List<ProductDto> searchProducts(String name) {
        return productRepository.findByNameContaining(name).stream()
            .map(ProductDto::from)
            .toList();
    }
    
    /**
     * 更新商品信息
     */
    public ProductDto updateProduct(String productId, String name, String description, 
                                  BigDecimal price, String currency) {
        Product product = productRepository.findById(ProductId.of(productId))
            .orElseThrow(() -> new BusinessRuleException("商品不存在: " + productId));
        
        Money newPrice = new Money(price, java.util.Currency.getInstance(currency));
        product.updateInfo(name, description, newPrice);
        
        productRepository.save(product);
        
        return ProductDto.from(product);
    }
    
    /**
     * 增加库存
     */
    public ProductDto increaseStock(String productId, int quantity) {
        Product product = productRepository.findById(ProductId.of(productId))
            .orElseThrow(() -> new BusinessRuleException("商品不存在: " + productId));
        
        product.increaseStock(quantity);
        productRepository.save(product);
        
        return ProductDto.from(product);
    }
    
    /**
     * 上架商品
     */
    public ProductDto putProductOnShelf(String productId) {
        Product product = productRepository.findById(ProductId.of(productId))
            .orElseThrow(() -> new BusinessRuleException("商品不存在: " + productId));
        
        product.putOnShelf();
        productRepository.save(product);
        
        return ProductDto.from(product);
    }
    
    /**
     * 下架商品
     */
    public ProductDto takeProductOffShelf(String productId) {
        Product product = productRepository.findById(ProductId.of(productId))
            .orElseThrow(() -> new BusinessRuleException("商品不存在: " + productId));
        
        product.takeOffShelf();
        productRepository.save(product);
        
        return ProductDto.from(product);
    }
}