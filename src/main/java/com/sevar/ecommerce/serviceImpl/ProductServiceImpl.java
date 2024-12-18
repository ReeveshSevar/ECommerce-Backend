package com.sevar.ecommerce.serviceImpl;

import com.sevar.ecommerce.exception.ProductException;
import com.sevar.ecommerce.model.Category;
import com.sevar.ecommerce.model.Product;
import com.sevar.ecommerce.model.dto.ProductDto;
import com.sevar.ecommerce.repository.CategoryRepository;
import com.sevar.ecommerce.repository.ProductRepository;
import com.sevar.ecommerce.service.ProductService;
import com.sevar.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService
{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    @Override
    public Product createProduct(ProductDto productDto) {
        Category topLevel = categoryRepository.findByName(productDto.getTopLevelCategory());
        if (topLevel == null){
            Category topLevelCategory = new Category();
            topLevelCategory.setName(productDto.getTopLevelCategory());
            topLevelCategory.setLevel(1);
            topLevel = categoryRepository.save(topLevelCategory);
        }
        Category secondLevel = categoryRepository.findByNameAndParent(productDto.getSecondLevelCategory(),topLevel.getName());
        if (secondLevel == null){
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(productDto.getSecondLevelCategory());
            secondLevelCategory.setParentCategory(topLevel);
            secondLevelCategory.setLevel(2);
            secondLevel = categoryRepository.save(secondLevelCategory);
        }
        Category thirdLevel = categoryRepository.findByNameAndParent(productDto.getThirdLevelCategory(),secondLevel.getName());
        if (thirdLevel == null){
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(productDto.getThirdLevelCategory());
            thirdLevelCategory.setParentCategory(secondLevel);
            thirdLevelCategory.setLevel(3);
            thirdLevel = categoryRepository.save(thirdLevelCategory);
        }

        Product product = new Product();
        product.setTitle(productDto.getTitle());
        product.setColor(productDto.getColor());
        product.setDescription(productDto.getDescription());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setDiscountPercent(productDto.getDiscountPercent());
        String base64Image = productDto.getImage();
        if (base64Image != null && base64Image.startsWith("data:image")) {
            base64Image = base64Image.split(",")[1];
        }
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);
        product.setImage(imageBytes);
        product.setBrand(productDto.getBrand());
        product.setPrice(productDto.getPrice());
        product.setSizes(productDto.getSize());
        product.setQuantity(productDto.getQuantity());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    @Override
    public String deleteProduct(Long id) throws ProductException {
        Product product = findProductById(id);
        product.getSizes().clear();
        productRepository.deleteById(id);
        return "Product Deleted Successfully";
    }

    @Override
    public Product updateProduct(Long productId, Product req) throws ProductException {
        Product products = findProductById(productId);
        if (req.getQuantity() != 0){
            products.setQuantity(req.getQuantity());
        }
        return productRepository.save(products);
    }

    @Override
    public Product findProductById(Long id) throws ProductException {
        Optional<Product> opt = productRepository.findById(id);
        if (opt.isPresent()){
            return opt.get();
        }
        throw new ProductException("Product Not Found With Id: "+id);
    }

    @Override
    public List<Product> findAllProducts() throws ProductException {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findProductByCategoryId(String category) {
        return List.of();
    }

    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize)
    {

        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        List<Product> products = productRepository.filterProduct(category,minPrice,maxPrice,minDiscount,sort);
        if (!colors.isEmpty()){
            products = products.stream().filter(p-> colors.stream().anyMatch(c-> c.equalsIgnoreCase(p.getColor()))).collect(Collectors.toList());
        }
        if (stock!=null){
            if (stock.equals("in_stock")){
                products.stream().filter(p-> p.getQuantity()>0).collect(Collectors.toList());
            } else if (stock.equals("out_of_stock")) {
                products.stream().filter(p-> p.getQuantity()<1).collect(Collectors.toList());
            }
        }
        int startIndex = (int)pageable.getOffset();
        int endIndex = Math.min(startIndex+pageable.getPageSize(),products.size());

        List<Product> pageContent = products.subList(startIndex,endIndex);
        return new PageImpl<>(pageContent,pageable,products.size());
    }

    @Override
    public List<Product> searchProductByCategory(String category) {
        category = category.toLowerCase();
        return productRepository.searchProduct(category);
    }
}
