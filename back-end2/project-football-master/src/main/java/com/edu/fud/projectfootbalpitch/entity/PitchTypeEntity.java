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
@Table(name = "pitch_type")
public class PitchTypeEntity extends BaseEntity implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @OneToMany(mappedBy = "pitchTypeEntitySub",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<SubPitchEntity> subPitchEntities = new ArrayList<>();
}
