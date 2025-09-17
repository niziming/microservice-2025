package cn.zm.ddd.application.dto;

import cn.zm.ddd.domain.model.customer.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.hutool.core.bean.BeanUtil;

import java.time.LocalDateTime;

/**
 * 客户数据传输对象 - Lombok + Hutool优化版本
 * 
 * 在应用层使用Lombok的最佳实践：
 * 1. @Data - DTO作为数据载体，适合使用
 * 2. @Builder - 提供链式构建方式
 * 3. @NoArgsConstructor/@AllArgsConstructor - 支持多种构造方式
 * 4. 使用Hutool简化Bean转换
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private String id;
    private String name;
    private String email;
    private String customerType;
    private String customerTypeDescription;
    private boolean active;
    private boolean canReceiveDiscount;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    
    /**
     * 从领域对象转换为DTO - 使用Hutool优化
     */
    public static CustomerDto from(Customer customer) {
        CustomerDto dto = new CustomerDto();
        
        // 使用Hutool简化基础属性拷贝
        BeanUtil.copyProperties(customer, dto);
        
        // 手动设置需要特殊处理的属性
        dto.setId(customer.getId().value());
        dto.setEmail(customer.getEmail().value());
        dto.setCustomerType(customer.getType().name());
        dto.setCustomerTypeDescription(customer.getType().getDescription());
        dto.setCanReceiveDiscount(customer.canReceiveDiscount());
        
        return dto;
    }
}