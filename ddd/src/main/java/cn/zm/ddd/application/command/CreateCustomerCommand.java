package cn.zm.ddd.application.command;

import cn.zm.ddd.domain.model.customer.CustomerType;
import cn.zm.ddd.shared.valueobject.Email;

/**
 * 创建客户命令
 * 
 * CQRS模式中的命令对象：
 * 1. 表示用户的操作意图
 * 2. 包含执行操作所需的所有数据
 * 3. 不可变对象，确保数据完整性
 * 4. 提供验证逻辑
 */
public record CreateCustomerCommand(
    String name,
    String email,
    String customerType
) {
    
    public CreateCustomerCommand {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("客户姓名不能为空");
        }
        
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("邮箱不能为空");
        }
        
        if (customerType == null || customerType.trim().isEmpty()) {
            throw new IllegalArgumentException("客户类型不能为空");
        }
    }
    
    /**
     * 获取邮箱值对象
     */
    public Email getEmailValueObject() {
        return new Email(email);
    }
    
    /**
     * 获取客户类型枚举
     */
    public CustomerType getCustomerTypeEnum() {
        return CustomerType.valueOf(customerType.toUpperCase());
    }
}