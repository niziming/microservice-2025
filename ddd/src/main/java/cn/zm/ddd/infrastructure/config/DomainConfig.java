package cn.zm.ddd.infrastructure.config;

import cn.zm.ddd.domain.repository.ProductRepository;
import cn.zm.ddd.domain.service.OrderDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 领域服务配置
 * 
 * 配置领域服务的Spring Bean
 */
@Configuration
public class DomainConfig {
    
    /**
     * 配置订单领域服务
     */
    @Bean
    public OrderDomainService orderDomainService(ProductRepository productRepository) {
        return new OrderDomainService(productRepository);
    }
}