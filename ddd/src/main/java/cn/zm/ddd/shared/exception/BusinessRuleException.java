package cn.zm.ddd.shared.exception;

/**
 * 业务规则异常
 * 
 * 当违反业务规则时抛出此异常
 */
public class BusinessRuleException extends DomainException {
    
    public BusinessRuleException(String message) {
        super(message);
    }
    
    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}