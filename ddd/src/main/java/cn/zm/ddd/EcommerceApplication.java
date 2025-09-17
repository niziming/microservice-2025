package cn.zm.ddd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * DDD电商系统启动类 - MyBatis-Plus版本
 * 
 * 使用@MapperScan注解扫描Mapper接口
 */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("cn.zm.ddd.infrastructure.mapper")
public class EcommerceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
        System.out.println("DDD电商系统已启动（MyBatis-Plus版本）！");
        System.out.println("API文档地址: http://localhost:8080/doc.html");
    }
}