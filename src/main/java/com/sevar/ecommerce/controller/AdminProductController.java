package com.sevar.ecommerce.controller;

import com.sevar.ecommerce.exception.ProductException;
import com.sevar.ecommerce.model.Product;
import com.sevar.ecommerce.model.dto.ProductDto;
import com.sevar.ecommerce.response.ApiResponse;
import com.sevar.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto){
        Product product = productService.createProduct(productDto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) throws ProductException{
        productService.deleteProduct(productId);
        ApiResponse response = new ApiResponse();
        response.setMessage("Product Deleted Successfully");
        response.setStatus(true);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = productService.findAllProducts();
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product, @PathVariable Long productId) throws ProductException{
        Product products = productService.updateProduct(productId,product);
        return new ResponseEntity<Product>(products,HttpStatus.ACCEPTED);
    }

    @PutMapping("/creates")
    public ResponseEntity<ApiResponse> createMultipleProduct(@RequestBody ProductDto[] request) {
        for (ProductDto productDto : request){
            productService.createProduct(productDto);
        }
        ApiResponse response = new ApiResponse();
        response.setMessage("Product Created Successfully");
        response.setStatus(true);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
}
