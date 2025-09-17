package cn.zm.ddd.shared.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

/**
 * 金额值对象
 * 
 * DDD值对象的典型实现，包含：
 * 1. 不可变性 - 使用final字段和record
 * 2. 自验证 - 构造时验证业务规则
 * 3. 业务行为 - 提供金额计算方法
 * 4. 类型安全 - 避免直接使用BigDecimal造成的错误
 */
public record Money(BigDecimal amount, Currency currency) {
    
    public Money {
        // 验证金额不能为null且不能为负数
        if (amount == null) {
            throw new IllegalArgumentException("金额不能为空");
        }
        
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("金额不能为负数: " + amount);
        }
        
        if (currency == null) {
            throw new IllegalArgumentException("货币类型不能为空");
        }
        
        // 统一精度到2位小数
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * 创建人民币金额
     */
    public static Money cny(BigDecimal amount) {
        return new Money(amount, Currency.getInstance("CNY"));
    }
    
    /**
     * 创建美元金额
     */
    public static Money usd(BigDecimal amount) {
        return new Money(amount, Currency.getInstance("USD"));
    }
    
    /**
     * 创建零金额
     */
    public static Money zero(Currency currency) {
        return new Money(BigDecimal.ZERO, currency);
    }
    
    /**
     * 金额相加
     */
    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("不能对不同货币进行运算");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }
    
    /**
     * 金额相减
     */
    public Money subtract(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("不能对不同货币进行运算");
        }
        return new Money(this.amount.subtract(other.amount), this.currency);
    }
    
    /**
     * 金额乘法
     */
    public Money multiply(BigDecimal multiplier) {
        return new Money(this.amount.multiply(multiplier), this.currency);
    }
    
    /**
     * 比较金额大小
     */
    public boolean isGreaterThan(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("不能比较不同货币的金额");
        }
        return this.amount.compareTo(other.amount) > 0;
    }
    
    /**
     * 检查是否为零
     */
    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }
    
    @Override
    public String toString() {
        return String.format("%s %s", currency.getCurrencyCode(), amount);
    }
}