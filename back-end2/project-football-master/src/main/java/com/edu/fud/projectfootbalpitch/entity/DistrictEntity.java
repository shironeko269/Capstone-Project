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
@Table(name = "districts")
public class DistrictEntity extends BaseEntity implements Serializable {

    @Column(name = "district_name")
    private String districtName;

    @OneToMany(mappedBy = "districtEntity",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<WardEntity> wardEntities = new ArrayList<>();
}
