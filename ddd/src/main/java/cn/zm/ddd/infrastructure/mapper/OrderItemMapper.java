package cn.zm.ddd.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.zm.ddd.infrastructure.persistence.OrderItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;

import java.util.List;

/**
 * 订单项Mapper接口 - MyBatis-Plus版本
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItemEntity> {
    
    /**
     * 根据订单ID查找订单项
     */
    @Select("SELECT * FROM order_items WHERE order_id = #{orderId}")
    List<OrderItemEntity> findByOrderId(@Param("orderId") String orderId);
    
    /**
     * 删除指定订单的所有订单项
     */
    @Delete("DELETE FROM order_items WHERE order_id = #{orderId}")
    int deleteByOrderId(@Param("orderId") String orderId);
}