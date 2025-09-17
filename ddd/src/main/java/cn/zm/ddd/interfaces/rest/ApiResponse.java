package cn.zm.ddd.interfaces.rest;

/**
 * 统一API响应格式
 * 
 * 提供一致的API响应结构，包含状态、消息和数据
 */
public record ApiResponse<T>(
    boolean success,
    String message,
    T data,
    long timestamp
) {
    
    /**
     * 创建成功响应
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, System.currentTimeMillis());
    }
    
    /**
     * 创建成功响应（无数据）
     */
    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(true, message, null, System.currentTimeMillis());
    }
    
    /**
     * 创建错误响应
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, System.currentTimeMillis());
    }
    
    /**
     * 创建错误响应（带数据）
     */
    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(false, message, data, System.currentTimeMillis());
    }
}