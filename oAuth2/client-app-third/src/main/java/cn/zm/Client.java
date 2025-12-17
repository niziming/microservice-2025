package cn.zm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * 模拟前端
 * @author Simon.ni
 */
@SpringBootApplication
@EnableWebSecurity
public class Client {
    public static void main(String[] args) {
        SpringApplication.run(Client.class, args);
    }
}