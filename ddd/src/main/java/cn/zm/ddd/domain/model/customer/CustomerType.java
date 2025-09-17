package cn.zm.ddd.domain.model.customer;

/**
 * 客户类型枚举
 * 
 * 表示客户的不同类型，影响业务逻辑
 */
public enum CustomerType {
    REGULAR("普通客户"),
    VIP("VIP客户"),
    ENTERPRISE("企业客户");
    
    private final String description;
    
    CustomerType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * VIP客户享受折扣
     */
    public boolean isEligibleForDiscount() {
        return this == VIP || this == ENTERPRISE;
    }
}