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
@Table(name = "category_products")
public class CategoryProductEntity extends BaseEntity implements Serializable {

    @Column(name = "name")
    private String name;


    @OneToMany(mappedBy = "categoryProductEntity",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<ProductsEntity> productsEntitiesCategory = new ArrayList<>();
}
