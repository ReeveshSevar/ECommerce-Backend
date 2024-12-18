package com.sevar.ecommerce.service;

import com.sevar.ecommerce.exception.ProductException;
import com.sevar.ecommerce.model.Product;
import com.sevar.ecommerce.model.dto.ProductDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductDto productDto);

    String deleteProduct(Long id) throws ProductException;

    Product updateProduct(Long productId, Product req) throws ProductException;

    Product findProductById(Long id) throws ProductException;

    List<Product> findAllProducts() throws ProductException;

    List<Product> findProductByCategoryId(String category);

    Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes,
                                Integer minPrice, Integer maxPrice, Integer minDiscount,
                                String sort, String stock, Integer pageNumber, Integer pageSize
    );

    List<Product> searchProductByCategory(String category);
}
