package cn.zm.ddd.shared.exception;

/**
 * 领域异常基类
 * 
 * DDD中的领域异常特点：
 * 1. 表达业务规则违反
 * 2. 提供有意义的错误信息
 * 3. 便于上层应用处理
 */
public abstract class DomainException extends RuntimeException {
    
    public DomainException(String message) {
        super(message);
    }
    
    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}