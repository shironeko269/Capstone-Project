package com.edu.fud.projectfootbalpitch.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_detail")
public class OrderDetailEntity extends BaseEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity orderEntityOrderDetail;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductsEntity productsEntityOrderDetail;

    @Column(name = "quantity",nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;
}
