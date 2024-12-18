package com.sevar.ecommerce.repository;

import com.sevar.ecommerce.model.Address;
import com.sevar.ecommerce.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long>
{
    @Query("SELECT a FROM Address a WHERE a.streetAddress = :streetAddress AND a.city = :city AND a.state = :state AND a.zipCode = :zipCode AND a.users = :users")
    Optional<Address> findByUserAddress(
            String streetAddress,
            String city,
            String state,
            String zipCode,
            Users users
    );
}
