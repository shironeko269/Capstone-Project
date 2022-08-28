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
@Table(name = "is_member")
public class IsMemberEntity extends BaseEntity implements Serializable {

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "isMember", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserEntity> userEntityIsMember = new ArrayList<>();
}
