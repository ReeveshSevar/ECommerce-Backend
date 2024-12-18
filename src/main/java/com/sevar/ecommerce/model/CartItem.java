package com.sevar.ecommerce.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Cart cart;

    @ManyToOne
    private Product product;

    @Column(name = "sizes")
    private String size;

    private int quantity;

    private Integer discountedPrice;

    private Long userId;

    private Integer price;

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", product=" + product +
                ", size='" + size + '\'' +
                ", quantity=" + quantity +
                ", discountedPrice=" + discountedPrice +
                ", userId=" + userId +
                ", price=" + price +
                '}';
    }
}
