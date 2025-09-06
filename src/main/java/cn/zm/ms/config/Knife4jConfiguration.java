package cn.zm.ms.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfiguration {

    /**
     * 配置API文档基本信息
     * 同时通过application.yml中的paths-to-match指定扫描的Controller路径
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 文档基本信息
                .info(new Info()
                        .title("用户服务API文档")
                        .version("v1.0")
                        .description("用户服务的所有接口说明，包含用户CRUD等操作")
                        .contact(new Contact()
                                .name("开发者")
                                .email("dev@example.com")));
    }
}
