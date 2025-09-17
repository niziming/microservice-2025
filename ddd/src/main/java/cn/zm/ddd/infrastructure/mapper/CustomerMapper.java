package cn.zm.ddd.infrastructure.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.zm.ddd.infrastructure.persistence.CustomerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 客户Mapper接口 - MyBatis-Plus版本
 * 
 * MyBatis-Plus Mapper特点：
 * 1. 继承BaseMapper获得基础CRUD能力
 * 2. 使用注解或XML定义自定义查询
 * 3. 支持条件构造器进行复杂查询
 * 4. 自动生成SQL，减少手写SQL
 */
@Mapper
public interface CustomerMapper extends BaseMapper<CustomerEntity> {
    
    /**
     * 根据邮箱查找客户
     * 
     * @param email 邮箱地址
     * @return 客户实体
     */
    @Select("SELECT * FROM customers WHERE email = #{email}")
    CustomerEntity findByEmail(@Param("email") String email);
    
    /**
     * 检查邮箱是否存在
     * 
     * @param email 邮箱地址
     * @return 存在返回1，不存在返回0
     */
    @Select("SELECT COUNT(1) FROM customers WHERE email = #{email}")
    Integer existsByEmail(@Param("email") String email);
}