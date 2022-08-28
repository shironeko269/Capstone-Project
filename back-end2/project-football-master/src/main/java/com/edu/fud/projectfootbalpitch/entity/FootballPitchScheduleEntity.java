package com.edu.fud.projectfootbalpitch.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "football_pitchs_schedule")
public class FootballPitchScheduleEntity extends BaseEntity implements Serializable {

    @Column(name = "date_booking")
    private Date dateBooking;

    @Column(name = "time_start")
    private Time timeStart;

    @Column(name = "time_end")
    private Time timeEnd;

    @Column(name = "price")
    private double price;

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "sub_pitch_id")
    private SubPitchEntity subPitchEntitySchedule;

    @OneToMany(mappedBy = "footballPitchScheduleEntityService",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<PitchScheduleServiceEntity> pitchScheduleServiceEntities = new ArrayList<>();

    @OneToMany(mappedBy = "footballPitchScheduleEntityBooking",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<BookFootballPitchEntity> bookFootballPitchEntitiesUser = new ArrayList<>();
}
