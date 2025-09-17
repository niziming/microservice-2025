package cn.zm.ddd.application.query;

/**
 * 客户查询对象
 * 
 * CQRS模式中的查询对象：
 * 1. 表示查询的条件和参数
 * 2. 与命令分离，支持复杂的查询场景
 * 3. 可以包含分页、排序等参数
 */
public record CustomerQuery(
    String customerId,
    String email,
    String customerType,
    Boolean active
) {
    
    /**
     * 创建根据ID查询的实例
     */
    public static CustomerQuery byId(String customerId) {
        return new CustomerQuery(customerId, null, null, null);
    }
    
    /**
     * 创建根据邮箱查询的实例
     */
    public static CustomerQuery byEmail(String email) {
        return new CustomerQuery(null, email, null, null);
    }
    
    /**
     * 创建根据类型查询的实例
     */
    public static CustomerQuery byType(String customerType) {
        return new CustomerQuery(null, null, customerType, null);
    }
}