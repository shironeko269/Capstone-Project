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
@Table(name = "wards")
public class WardEntity extends BaseEntity implements Serializable {

    @ManyToOne
    @JoinColumn(name = "district_id")
    private DistrictEntity districtEntity;

    @Column(name = "ward_name")
    private String wardName;

    @OneToMany(mappedBy = "wardEntityPitch",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<FootballPitchEntity> footballPitchEntitiesWard = new ArrayList<>();
}
