package cn.zm.ddd.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.zm.ddd.infrastructure.persistence.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 订单Mapper接口 - MyBatis-Plus版本
 */
@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {
    
    /**
     * 根据客户ID查找订单
     */
    @Select("SELECT * FROM orders WHERE customer_id = #{customerId}")
    List<OrderEntity> findByCustomerId(@Param("customerId") String customerId);
    
    /**
     * 根据状态查找订单
     */
    @Select("SELECT * FROM orders WHERE status = #{status}")
    List<OrderEntity> findByStatus(@Param("status") String status);
    
    /**
     * 根据客户ID和状态查找订单
     */
    @Select("SELECT * FROM orders WHERE customer_id = #{customerId} AND status = #{status}")
    List<OrderEntity> findByCustomerIdAndStatus(@Param("customerId") String customerId, 
                                              @Param("status") String status);
}