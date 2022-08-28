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
@Table(name = "booking_status")
public class StatusBookFootballPitchEntity extends BaseEntity implements Serializable {
    @Column(name = "name",nullable = false)
    private String name;

    @OneToMany(mappedBy = "statusBookFootballPitchEntityBooking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookFootballPitchEntity> bookFootballPitchEntitiesStatus = new ArrayList<>();
}
