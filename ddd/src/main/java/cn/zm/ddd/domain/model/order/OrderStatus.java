package cn.zm.ddd.domain.model.order;

/**
 * 订单状态枚举
 * 
 * 表示订单在生命周期中的不同状态
 */
public enum OrderStatus {
    PENDING("待支付"),
    PAID("已支付"),
    SHIPPED("已发货"),
    DELIVERED("已送达"),
    CANCELLED("已取消"),
    REFUNDED("已退款");
    
    private final String description;
    
    OrderStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 检查是否可以取消
     */
    public boolean canBeCancelled() {
        return this == PENDING || this == PAID;
    }
    
    /**
     * 检查是否可以发货
     */
    public boolean canBeShipped() {
        return this == PAID;
    }
    
    /**
     * 检查是否可以退款
     */
    public boolean canBeRefunded() {
        return this == PAID || this == SHIPPED || this == DELIVERED;
    }
}