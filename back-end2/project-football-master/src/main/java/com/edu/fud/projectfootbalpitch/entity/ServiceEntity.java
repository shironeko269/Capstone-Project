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
@Table(name = "services")
public class ServiceEntity extends BaseEntity implements Serializable {

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "image",nullable = true)
    private String image;

    @Column(name = "unit",nullable = false)
    private String unit;

    @Column(name = "quantity",nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;

    @Column(name = "status",nullable = true)
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "category_service_id")
    private CategoryServiceEntity categoryServiceEntity;

    @OneToMany(mappedBy = "serviceEntityPitch",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<PitchScheduleServiceEntity> pitchScheduleServiceEntities = new ArrayList<>();
}
