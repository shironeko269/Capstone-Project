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
@Table(name = "products")
public class ProductsEntity extends BaseEntity implements Serializable {

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "quantity",nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;

    @Column(name = "unit",nullable = false)
    private String unit;

    @Column(name = "image",nullable = true)
    private String image;

    @Column(name = "discount",nullable = false)
    private double discount;

    @Column(name = "description",nullable = false,columnDefinition = "nvarchar(5000)")
    private String description;

    @Column(name = "status",nullable = true)
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private SupplierEntity supplierEntity;

    @ManyToOne
    @JoinColumn(name = "category_product_id")
    private CategoryProductEntity categoryProductEntity;

    @OneToMany(mappedBy = "productsEntityOrderDetail",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<OrderDetailEntity> orderDetailEntities = new ArrayList<>();
}
