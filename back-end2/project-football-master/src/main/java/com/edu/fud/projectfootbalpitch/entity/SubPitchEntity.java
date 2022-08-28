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
@Table(name = "sub_pitch")
public class SubPitchEntity extends BaseEntity implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "image1")
    private String image1;

    @Column(name = "image2")
    private String image2;

    @Column(name = "image3")
    private String image3;

    @Column(name = "image4")
    private String image4;

    @Column(name = "image5")
    private String image5;
    @ManyToOne
    @JoinColumn(name = "pitch_type_id")
    private PitchTypeEntity pitchTypeEntitySub;

    @ManyToOne
    @JoinColumn(name = "pitch_pitch_id")
    private FootballPitchEntity footballPitchEntitySub;

    @OneToMany(mappedBy = "subPitchEntitySchedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FootballPitchScheduleEntity> footballPitchScheduleEntitiesSubPitch = new ArrayList<>();
}
