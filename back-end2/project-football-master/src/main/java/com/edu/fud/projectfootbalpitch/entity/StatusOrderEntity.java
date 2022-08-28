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
@Table(name = "order_status")
public class StatusOrderEntity extends BaseEntity implements Serializable {

    @Column(name = "name",nullable = false)
    private String name;

    @OneToMany(mappedBy = "statusOrderEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderEntity> orderEntitiesOrder = new ArrayList<>();
}
