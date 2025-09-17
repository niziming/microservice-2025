package cn.zm.ddd.infrastructure.repository;

import cn.zm.ddd.domain.model.product.Product;
import cn.zm.ddd.domain.model.product.ProductId;
import cn.zm.ddd.domain.repository.ProductRepository;
import cn.zm.ddd.infrastructure.mapper.ProductMapper;
import cn.zm.ddd.infrastructure.persistence.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 商品仓储实现 - MyBatis-Plus版本
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    
    private final ProductMapper productMapper;
    
    public ProductRepositoryImpl(ProductMapper productMapper) {
        this.productMapper = Objects.requireNonNull(productMapper, "商品Mapper不能为空");
    }
    
    @Override
    public void save(Product product) {
        ProductEntity entity = ProductEntity.from(product);
        
        ProductEntity existingEntity = productMapper.selectById(product.getId().value());
        
        if (existingEntity != null) {
            entity.updateFrom(product);
            productMapper.updateById(entity);
        } else {
            productMapper.insert(entity);
        }
    }
    
    @Override
    public Optional<Product> findById(ProductId productId) {
        ProductEntity entity = productMapper.selectById(productId.value());
        return entity != null ? Optional.of(entity.toDomain()) : Optional.empty();
    }
    
    @Override
    public List<Product> findAllAvailable() {
        return productMapper.findByAvailableTrue().stream()
            .map(ProductEntity::toDomain)
            .toList();
    }
    
    @Override
    public List<Product> findByNameContaining(String name) {
        return productMapper.findByNameContainingIgnoreCase(name).stream()
            .map(ProductEntity::toDomain)
            .toList();
    }
    
    @Override
    public List<Product> findLowStockProducts(int threshold) {
        return productMapper.findLowStockProducts(threshold).stream()
            .map(ProductEntity::toDomain)
            .toList();
    }
    
    @Override
    public void deleteById(ProductId productId) {
        productMapper.deleteById(productId.value());
    }
}