package cn.zm.ddd.shared.valueobject;

import java.util.Objects;

/**
 * 邮箱地址值对象
 * 
 * DDD值对象特点：
 * 1. 不可变性 - 一旦创建不能修改
 * 2. 无标识性 - 通过值的内容来识别，而不是ID
 * 3. 业务逻辑集中 - 封装了邮箱验证逻辑
 * 4. 类型安全 - 避免原始类型的滥用
 */
public record Email(String value) {
    
    public Email {
        // 验证邮箱格式
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("邮箱地址不能为空");
        }
        
        if (!isValidEmail(value)) {
            throw new IllegalArgumentException("邮箱格式不正确: " + value);
        }
    }
    
    /**
     * 验证邮箱格式的私有方法
     */
    private static boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
    
    /**
     * 获取邮箱的域名部分
     */
    public String getDomain() {
        return value.substring(value.indexOf('@') + 1);
    }
    
    /**
     * 获取邮箱的用户名部分
     */
    public String getLocalPart() {
        return value.substring(0, value.indexOf('@'));
    }
    
    @Override
    public String toString() {
        return value;
    }
}