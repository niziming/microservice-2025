package cn.zm.ddd.application.service;

import cn.zm.ddd.application.command.CreateCustomerCommand;
import cn.zm.ddd.shared.valueobject.Email;
import cn.zm.ddd.application.dto.CustomerDto;
import cn.zm.ddd.application.query.CustomerQuery;
import cn.zm.ddd.domain.model.customer.Customer;
import cn.zm.ddd.domain.model.customer.CustomerId;
import cn.zm.ddd.domain.repository.CustomerRepository;
import cn.zm.ddd.shared.exception.BusinessRuleException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * 客户应用服务 - Lombok + Hutool优化版本
 * 
 * 在应用层使用Lombok和Hutool的最佳实践：
 * 1. @RequiredArgsConstructor - 自动生成依赖注入构造函数
 * 2. @Slf4j - 自动生成日志对象
 * 3. Hutool工具类 - 用于参数验证和工具方法
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerApplicationService {
    
    private final CustomerRepository customerRepository;
    
    /**
     * 创建客户 - 使用Hutool进行参数验证
     */
    public CustomerDto createCustomer(CreateCustomerCommand command) {
        log.info("开始创建客户: {}", command.name());
        
        // 使用Hutool进行参数验证
        validateCreateCustomerCommand(command);
        
        // 验证邮箱是否已存在
        if (customerRepository.existsByEmail(command.getEmailValueObject())) {
            throw new BusinessRuleException("邮箱已存在: " + command.email());
        }
        
        // 创建客户领域对象
        Customer customer = Customer.create(
            command.name(),
            command.getEmailValueObject(),
            command.getCustomerTypeEnum()
        );
        
        // 保存客户
        customerRepository.save(customer);
        
        log.info("客户创建成功: {}", customer.getId().value());
        
        // 返回DTO
        return CustomerDto.from(customer);
    }
    
    /**
     * 查询客户
     */
    @Transactional(readOnly = true)
    public Optional<CustomerDto> findCustomer(CustomerQuery query) {
        log.debug("查询客户: {}", query);
        
        Optional<Customer> customer = Optional.empty();
        
        // 根据不同查询条件查找客户
        if (StrUtil.isNotBlank(query.customerId())) {
            customer = customerRepository.findById(CustomerId.of(query.customerId()));
        } else if (StrUtil.isNotBlank(query.email())) {
            customer = customerRepository.findByEmail(
                new Email(query.email()));
        }
        
        return customer.map(CustomerDto::from);
    }
    
    /**
     * 升级客户为VIP
     */
    public CustomerDto upgradeToVip(String customerId) {
        log.info("升级客户为VIP: {}", customerId);
        
        // 使用Hutool验证参数
        if (StrUtil.isBlank(customerId)) {
            throw new IllegalArgumentException("客户ID不能为空");
        }
        
        Customer customer = customerRepository.findById(CustomerId.of(customerId))
            .orElseThrow(() -> new BusinessRuleException("客户不存在: " + customerId));
        
        // 调用领域对象的业务方法
        customer.upgradeToVip();
        
        // 保存变更
        customerRepository.save(customer);
        
        return CustomerDto.from(customer);
    }
    
    /**
     * 更新客户信息
     */
    public CustomerDto updateCustomer(String customerId, String newName, String newEmail) {
        log.info("更新客户信息: {}", customerId);
        
        // 参数验证
        validateUpdateParameters(customerId, newName, newEmail);
        
        Customer customer = customerRepository.findById(CustomerId.of(customerId))
            .orElseThrow(() -> new BusinessRuleException("客户不存在: " + customerId));
        
        // 验证新邮箱是否被其他客户使用
        var newEmailValueObject = new Email(newEmail);
        if (!customer.getEmail().equals(newEmailValueObject) && 
            customerRepository.existsByEmail(newEmailValueObject)) {
            throw new BusinessRuleException("邮箱已被其他客户使用: " + newEmail);
        }
        
        // 调用领域对象的业务方法
        customer.updateInfo(newName, newEmailValueObject);
        
        // 保存变更
        customerRepository.save(customer);
        
        return CustomerDto.from(customer);
    }
    
    /**
     * 停用客户
     */
    public CustomerDto deactivateCustomer(String customerId) {
        log.info("停用客户: {}", customerId);
        
        Customer customer = findCustomerById(customerId);
        customer.deactivate();
        customerRepository.save(customer);
        
        return CustomerDto.from(customer);
    }
    
    /**
     * 激活客户
     */
    public CustomerDto activateCustomer(String customerId) {
        log.info("激活客户: {}", customerId);
        
        Customer customer = findCustomerById(customerId);
        customer.activate();
        customerRepository.save(customer);
        
        return CustomerDto.from(customer);
    }
    
    /**
     * 验证创建客户命令参数 - 使用Hutool
     */
    private void validateCreateCustomerCommand(CreateCustomerCommand command) {
        if (ObjectUtil.isNull(command)) {
            throw new IllegalArgumentException("创建客户命令不能为空");
        }
        
        if (StrUtil.isBlank(command.name())) {
            throw new IllegalArgumentException("客户姓名不能为空");
        }
        
        if (StrUtil.isBlank(command.email())) {
            throw new IllegalArgumentException("邮箱不能为空");
        }
        
        if (StrUtil.isBlank(command.customerType())) {
            throw new IllegalArgumentException("客户类型不能为空");
        }
    }
    
    /**
     * 验证更新参数 - 使用Hutool
     */
    private void validateUpdateParameters(String customerId, String newName, String newEmail) {
        if (StrUtil.isBlank(customerId)) {
            throw new IllegalArgumentException("客户ID不能为空");
        }
        
        if (StrUtil.isBlank(newName)) {
            throw new IllegalArgumentException("新姓名不能为空");
        }
        
        if (StrUtil.isBlank(newEmail)) {
            throw new IllegalArgumentException("新邮箱不能为空");
        }
    }
    
    /**
     * 按ID查找客户的通用方法
     */
    private Customer findCustomerById(String customerId) {
        if (StrUtil.isBlank(customerId)) {
            throw new IllegalArgumentException("客户ID不能为空");
        }
        
        return customerRepository.findById(CustomerId.of(customerId))
            .orElseThrow(() -> new BusinessRuleException("客户不存在: " + customerId));
    }
}