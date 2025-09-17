package cn.zm.ddd.infrastructure.repository;

import cn.zm.ddd.domain.model.customer.Customer;
import cn.zm.ddd.domain.model.customer.CustomerId;
import cn.zm.ddd.domain.repository.CustomerRepository;
import cn.zm.ddd.infrastructure.mapper.CustomerMapper;
import cn.zm.ddd.infrastructure.persistence.CustomerEntity;
import cn.zm.ddd.shared.valueobject.Email;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

/**
 * 客户仓储实现 - MyBatis-Plus版本
 * 
 * MyBatis-Plus仓储实现的特点：
 * 1. 使用Mapper接口进行数据访问
 * 2. 利用BaseMapper提供的基础CRUD操作
 * 3. 处理领域对象与持久化实体之间的转换
 * 4. 保持与领域层接口的一致性
 */
@Repository
public class CustomerRepositoryImpl implements CustomerRepository {
    
    private final CustomerMapper customerMapper;
    
    public CustomerRepositoryImpl(CustomerMapper customerMapper) {
        this.customerMapper = Objects.requireNonNull(customerMapper, "客户Mapper不能为空");
    }
    
    @Override
    public void save(Customer customer) {
        CustomerEntity entity = CustomerEntity.from(customer);
        
        // 检查是否已存在
        CustomerEntity existingEntity = customerMapper.selectById(customer.getId().value());
        
        if (existingEntity != null) {
            // 更新现有实体
            entity.updateFrom(customer);
            customerMapper.updateById(entity);
        } else {
            // 插入新实体
            customerMapper.insert(entity);
        }
    }
    
    @Override
    public Optional<Customer> findById(CustomerId customerId) {
        CustomerEntity entity = customerMapper.selectById(customerId.value());
        return entity != null ? Optional.of(entity.toDomain()) : Optional.empty();
    }
    
    @Override
    public Optional<Customer> findByEmail(Email email) {
        CustomerEntity entity = customerMapper.findByEmail(email.value());
        return entity != null ? Optional.of(entity.toDomain()) : Optional.empty();
    }
    
    @Override
    public boolean existsByEmail(Email email) {
        Integer count = customerMapper.existsByEmail(email.value());
        return count != null && count > 0;
    }
    
    @Override
    public void deleteById(CustomerId customerId) {
        customerMapper.deleteById(customerId.value());
    }
}