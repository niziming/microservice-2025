package cn.zm.ddd.domain.repository;

import cn.zm.ddd.domain.model.customer.Customer;
import cn.zm.ddd.domain.model.customer.CustomerId;
import cn.zm.ddd.shared.valueobject.Email;

import java.util.Optional;

/**
 * 客户仓储接口
 * 
 * DDD仓储模式特点：
 * 1. 位于领域层，定义领域需要的数据访问接口
 * 2. 屏蔽数据访问技术细节
 * 3. 使用领域对象作为参数和返回值
 * 4. 提供面向业务的查询方法
 */
public interface CustomerRepository {
    
    /**
     * 保存客户
     * 
     * @param customer 客户聚合根
     */
    void save(Customer customer);
    
    /**
     * 根据ID查找客户
     * 
     * @param customerId 客户ID
     * @return 客户实体，如果不存在则返回empty
     */
    Optional<Customer> findById(CustomerId customerId);
    
    /**
     * 根据邮箱查找客户
     * 
     * @param email 邮箱地址
     * @return 客户实体，如果不存在则返回empty
     */
    Optional<Customer> findByEmail(Email email);
    
    /**
     * 检查邮箱是否已存在
     * 
     * @param email 邮箱地址
     * @return 如果邮箱已存在返回true
     */
    boolean existsByEmail(Email email);
    
    /**
     * 删除客户
     * 
     * @param customerId 客户ID
     */
    void deleteById(CustomerId customerId);
}