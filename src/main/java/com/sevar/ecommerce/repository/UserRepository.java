package com.sevar.ecommerce.repository;
import com.sevar.ecommerce.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users,Long> {
    boolean existsByEmail(String email);

    void deleteByEmail(String email);

    Users findByEmail(String email);
}
