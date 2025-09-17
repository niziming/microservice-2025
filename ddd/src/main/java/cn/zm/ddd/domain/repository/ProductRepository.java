package cn.zm.ddd.domain.repository;

import cn.zm.ddd.domain.model.product.Product;
import cn.zm.ddd.domain.model.product.ProductId;

import java.util.List;
import java.util.Optional;

/**
 * 商品仓储接口
 */
public interface ProductRepository {
    
    /**
     * 保存商品
     */
    void save(Product product);
    
    /**
     * 根据ID查找商品
     */
    Optional<Product> findById(ProductId productId);
    
    /**
     * 查找所有可用商品
     */
    List<Product> findAllAvailable();
    
    /**
     * 根据名称搜索商品
     */
    List<Product> findByNameContaining(String name);
    
    /**
     * 查找库存不足的商品
     */
    List<Product> findLowStockProducts(int threshold);
    
    /**
     * 删除商品
     */
    void deleteById(ProductId productId);
}