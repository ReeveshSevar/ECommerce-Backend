package com.sevar.ecommerce.controller;

import com.sevar.ecommerce.model.Product;
import com.sevar.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sevar")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "colors", required = false) List<String> colors,
            @RequestParam(value = "sizes", required = false) List<String> sizes,
            @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
            @RequestParam(value = "minDiscount", required = false) Integer minDiscount,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "stock", required = false) String stock,
            Pageable pageable) {
        if (colors == null )
            colors = new ArrayList<>();
        if (minPrice == 0)
            minPrice = null;
        if (maxPrice == 0 )
            maxPrice = null;
        if (sizes != null) {
            sizes = sizes.stream()
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());
        }
        Page<Product> products = productService.getAllProduct(category, colors, sizes, minPrice, maxPrice, minDiscount, sort, stock, pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = productService.findProductById(productId);
        return new ResponseEntity<Product>(product, HttpStatus.ACCEPTED);
    }

    @GetMapping("/products/all")
    public ResponseEntity<List<Product>> findAllProducts(){
        List<Product> list = productService.findAllProducts();
        return new ResponseEntity<List<Product>>(list,HttpStatus.OK);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProductsByCategory(@RequestParam(value = "category", required = false) String category)
    {
        List<Product> products = productService.searchProductByCategory(category);
        return ResponseEntity.ok(products);
    }
}
