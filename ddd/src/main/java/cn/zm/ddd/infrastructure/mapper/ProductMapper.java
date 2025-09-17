package cn.zm.ddd.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.zm.ddd.infrastructure.persistence.ProductEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品Mapper接口 - MyBatis-Plus版本
 */
@Mapper
public interface ProductMapper extends BaseMapper<ProductEntity> {
    
    /**
     * 查找所有可用商品
     */
    @Select("SELECT * FROM products WHERE available = true")
    List<ProductEntity> findByAvailableTrue();
    
    /**
     * 根据名称搜索商品（忽略大小写）
     */
    @Select("SELECT * FROM products WHERE UPPER(name) LIKE UPPER(CONCAT('%', #{name}, '%'))")
    List<ProductEntity> findByNameContainingIgnoreCase(@Param("name") String name);
    
    /**
     * 查找库存不足的商品
     */
    @Select("SELECT * FROM products WHERE stock_quantity <= #{threshold} AND available = true")
    List<ProductEntity> findLowStockProducts(@Param("threshold") int threshold);
}