package cn.zm.ddd.interfaces.rest;

import cn.zm.ddd.application.command.CreateProductCommand;
import cn.zm.ddd.application.dto.ProductDto;
import cn.zm.ddd.application.service.ProductApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 商品REST控制器
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    private final ProductApplicationService productApplicationService;
    
    public ProductController(ProductApplicationService productApplicationService) {
        this.productApplicationService = Objects.requireNonNull(
            productApplicationService, "商品应用服务不能为空");
    }
    
    /**
     * 创建商品
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(
            @RequestBody CreateProductRequest request) {
        
        CreateProductCommand command = new CreateProductCommand(
            request.name(),
            request.description(),
            request.price(),
            request.currency(),
            request.stockQuantity()
        );
        
        ProductDto productDto = productApplicationService.createProduct(command);
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("商品创建成功", productDto));
    }
    
    /**
     * 查询商品
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDto>> getProduct(
            @PathVariable String productId) {
        
        Optional<ProductDto> product = productApplicationService.findProduct(productId);
        
        if (product.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success("查询成功", product.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error("商品不存在"));
        }
    }
    
    /**
     * 查询所有可用商品
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllAvailableProducts() {
        List<ProductDto> products = productApplicationService.findAllAvailableProducts();
        
        return ResponseEntity.ok(ApiResponse.success("查询成功", products));
    }
    
    /**
     * 搜索商品
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductDto>>> searchProducts(
            @RequestParam String name) {
        
        List<ProductDto> products = productApplicationService.searchProducts(name);
        
        return ResponseEntity.ok(ApiResponse.success("搜索成功", products));
    }
    
    /**
     * 更新商品信息
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @PathVariable String productId,
            @RequestBody UpdateProductRequest request) {
        
        ProductDto productDto = productApplicationService.updateProduct(
            productId, request.name(), request.description(), 
            request.price(), request.currency());
        
        return ResponseEntity.ok(ApiResponse.success("商品更新成功", productDto));
    }
    
    /**
     * 增加库存
     */
    @PostMapping("/{productId}/increase-stock")
    public ResponseEntity<ApiResponse<ProductDto>> increaseStock(
            @PathVariable String productId,
            @RequestBody IncreaseStockRequest request) {
        
        ProductDto productDto = productApplicationService.increaseStock(
            productId, request.quantity());
        
        return ResponseEntity.ok(ApiResponse.success("库存增加成功", productDto));
    }
    
    /**
     * 上架商品
     */
    @PostMapping("/{productId}/put-on-shelf")
    public ResponseEntity<ApiResponse<ProductDto>> putOnShelf(
            @PathVariable String productId) {
        
        ProductDto productDto = productApplicationService.putProductOnShelf(productId);
        
        return ResponseEntity.ok(ApiResponse.success("商品已上架", productDto));
    }
    
    /**
     * 下架商品
     */
    @PostMapping("/{productId}/take-off-shelf")
    public ResponseEntity<ApiResponse<ProductDto>> takeOffShelf(
            @PathVariable String productId) {
        
        ProductDto productDto = productApplicationService.takeProductOffShelf(productId);
        
        return ResponseEntity.ok(ApiResponse.success("商品已下架", productDto));
    }
}

/**
 * 创建商品请求
 */
record CreateProductRequest(
    String name,
    String description,
    BigDecimal price,
    String currency,
    int stockQuantity
) {}

/**
 * 更新商品请求
 */
record UpdateProductRequest(
    String name,
    String description,
    BigDecimal price,
    String currency
) {}

/**
 * 增加库存请求
 */
record IncreaseStockRequest(
    int quantity
) {}