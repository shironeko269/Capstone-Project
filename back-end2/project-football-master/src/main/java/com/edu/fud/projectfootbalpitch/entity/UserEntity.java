package com.edu.fud.projectfootbalpitch.entity;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity implements Serializable{

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "gmail")
    private String gmail;

    @Column(name = "address")
    private String address;

    @Column(name = "image")
    private String image;

    @Column(name = "role")
    private String role;

    @ManyToOne
    @JoinColumn(name = "is_member")
    private IsMemberEntity isMember;

    @OneToMany(mappedBy = "userEntityOrder",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<OrderEntity> orderEntitiesUser =new ArrayList<>();

    @OneToMany(mappedBy = "userEntityPitch",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<FootballPitchEntity> footballPitchEntitiesUser = new ArrayList<>();

    @OneToMany(mappedBy = "userEntityPitchBooking",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<BookFootballPitchEntity> bookFootballPitchEntitiesUser = new ArrayList<>();

    @PreRemove
    public void deleteUserEmployee(){
            footballPitchEntitiesUser.forEach(footballPitchEntity -> footballPitchEntity.setUserEntityPitch(null));
    }
}
