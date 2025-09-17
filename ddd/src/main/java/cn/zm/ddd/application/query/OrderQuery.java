package cn.zm.ddd.application.query;

/**
 * 订单查询对象
 */
public record OrderQuery(
    String orderId,
    String customerId,
    String status
) {
    
    /**
     * 根据订单ID查询
     */
    public static OrderQuery byId(String orderId) {
        return new OrderQuery(orderId, null, null);
    }
    
    /**
     * 根据客户ID查询
     */
    public static OrderQuery byCustomerId(String customerId) {
        return new OrderQuery(null, customerId, null);
    }
    
    /**
     * 根据状态查询
     */
    public static OrderQuery byStatus(String status) {
        return new OrderQuery(null, null, status);
    }
    
    /**
     * 根据客户ID和状态查询
     */
    public static OrderQuery byCustomerIdAndStatus(String customerId, String status) {
        return new OrderQuery(null, customerId, status);
    }
}