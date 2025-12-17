package cn.zm.ddd.interfaces.rest;

import cn.hutool.core.util.StrUtil;
import cn.zm.ddd.application.command.CreateCustomerCommand;
import cn.zm.ddd.application.dto.CustomerDto;
import cn.zm.ddd.application.query.CustomerQuery;
import cn.zm.ddd.application.service.CustomerApplicationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * 客户REST控制器 - Lombok + Hutool优化版本
 * <p>
 * 在接口层使用Lombok的最佳实践：
 * 1. @RequiredArgsConstructor - 自动生成依赖注入构造函数
 * 2. @Slf4j - 自动生成日志对象
 * 3. @Data - 用于请求/响应对象
 * 4. Hutool - 用于参数验证和工具方法
 */
@Slf4j
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerApplicationService customerApplicationService;

    /**
     * 创建客户
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDto>> createCustomer(
            @RequestBody CreateCustomerRequest request) {

        System.out.printf("接收创建客户请求: %s", request.getName());

        // 使用Hutool验证请求参数
        validateCreateCustomerRequest(request);

        CreateCustomerCommand command = new CreateCustomerCommand(
                request.getName(),
                request.getEmail(),
                request.getCustomerType()
        );

        CustomerDto customerDto = customerApplicationService.createCustomer(command);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("客户创建成功", customerDto));
    }

    /**
     * 根据ID查询客户
     */
    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomer(
            @PathVariable String customerId) {

        System.out.printf("查询客户: %s", customerId);

        // 使用Hutool验证参数
        if (StrUtil.isBlank(customerId)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("客户ID不能为空"));
        }

        Optional<CustomerDto> customer = customerApplicationService
                .findCustomer(CustomerQuery.byId(customerId));

        if (customer.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("查询成功", customer.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("客户不存在"));
        }
    }

    /**
     * 升级客户为VIP
     */
    @PostMapping("/{customerId}/upgrade-to-vip")
    public ResponseEntity<ApiResponse<CustomerDto>> upgradeToVip(
            @PathVariable String customerId) {

        System.out.printf("升级客户为VIP: %s", customerId);

        CustomerDto customerDto = customerApplicationService.upgradeToVip(customerId);

        return ResponseEntity.ok(ApiResponse.success("客户已升级为VIP", customerDto));
    }

    /**
     * 更新客户信息
     */
    @PutMapping("/{customerId}")
    public ResponseEntity<ApiResponse<CustomerDto>> updateCustomer(
            @PathVariable String customerId,
            @RequestBody UpdateCustomerRequest request) {

        System.out.printf("更新客户信息: %s", customerId);

        // 验证请求参数
        validateUpdateCustomerRequest(request);

        CustomerDto customerDto = customerApplicationService.updateCustomer(
                customerId, request.getName(), request.getEmail());

        return ResponseEntity.ok(ApiResponse.success("客户信息更新成功", customerDto));
    }

    /**
     * 停用客户
     */
    @PostMapping("/{customerId}/deactivate")
    public ResponseEntity<ApiResponse<CustomerDto>> deactivateCustomer(
            @PathVariable String customerId) {

        System.out.printf("停用客户: %s", customerId);

        CustomerDto customerDto = customerApplicationService.deactivateCustomer(customerId);

        return ResponseEntity.ok(ApiResponse.success("客户已停用", customerDto));
    }

    /**
     * 激活客户
     */
    @PostMapping("/{customerId}/activate")
    public ResponseEntity<ApiResponse<CustomerDto>> activateCustomer(
            @PathVariable String customerId) {

        System.out.printf("激活客户: %s", customerId);

        CustomerDto customerDto = customerApplicationService.activateCustomer(customerId);

        return ResponseEntity.ok(ApiResponse.success("客户已激活", customerDto));
    }

    /**
     * 验证创建客户请求参数
     */
    private void validateCreateCustomerRequest(CreateCustomerRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("请求参数不能为空");
        }

        if (StrUtil.isBlank(request.getName())) {
            throw new IllegalArgumentException("客户姓名不能为空");
        }

        if (StrUtil.isBlank(request.getEmail())) {
            throw new IllegalArgumentException("邮箱不能为空");
        }

        if (StrUtil.isBlank(request.getCustomerType())) {
            throw new IllegalArgumentException("客户类型不能为空");
        }
    }

    /**
     * 验证更新客户请求参数
     */
    private void validateUpdateCustomerRequest(UpdateCustomerRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("请求参数不能为空");
        }

        if (StrUtil.isBlank(request.getName())) {
            throw new IllegalArgumentException("姓名不能为空");
        }

        if (StrUtil.isBlank(request.getEmail())) {
            throw new IllegalArgumentException("邮箱不能为空");
        }
    }
}

/**
 * 创建客户请求 - 使用Lombok简化
 */
@Data
class CreateCustomerRequest {
    private String name;
    private String email;
    private String customerType;
}

/**
 * 更新客户请求 - 使用Lombok简化
 */
@Data
class UpdateCustomerRequest {
    private String name;
    private String email;
}