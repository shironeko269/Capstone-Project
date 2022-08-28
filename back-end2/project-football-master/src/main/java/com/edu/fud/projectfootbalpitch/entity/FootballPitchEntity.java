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
@Table(name = "football_pitchs")
public class FootballPitchEntity extends BaseEntity implements Serializable {

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "description",nullable = false)
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "street_number",nullable = false)
    private String streetNumber;

    @Column(name = "urlmap",columnDefinition = "nvarchar(500) not null")
    private String urlMap;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntityPitch;

    @ManyToOne
    @JoinColumn(name = "ward_id")
    private WardEntity wardEntityPitch;

    @OneToMany(mappedBy = "footballPitchEntitySub",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<SubPitchEntity> subPitchEntitiesPitch = new ArrayList<>();
}
