package com.edu.fud.projectfootbalpitch.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pitch_schedule_service")
public class PitchScheduleServiceEntity extends BaseEntity implements Serializable {
    @ManyToOne
    @JoinColumn(name = "footballPitchSchedule_id")
    private FootballPitchScheduleEntity footballPitchScheduleEntityService;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity serviceEntityPitch;

    @Column(name = "quantity",nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;
}
