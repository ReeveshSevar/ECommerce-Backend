package com.sevar.ecommerce.repository;
import com.sevar.ecommerce.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long>
{
    @Query("SELECT r FROM Review r WHERE r.product.id = :productId")
    List<Review> getAllReviewsForProduct(@Param("productId") Long productId);

    @Query("SELECT r FROM Review r WHERE r.product.id = :productId")
    Review getReviewsForProduct(@Param("productId") Long productId);
}
