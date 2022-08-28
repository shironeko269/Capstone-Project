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
@Table(name = "book_football_pitch")
public class BookFootballPitchEntity extends BaseEntity implements Serializable {

    @Column(name = "pre_order_payment")
    private double PreOrderPayment;

    @Column(name = "note")
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntityPitchBooking;

    @ManyToOne
    @JoinColumn(name = "football_pitch_schedule_id")
    private FootballPitchScheduleEntity footballPitchScheduleEntityBooking;

    @ManyToOne
    @JoinColumn(name = "football_pitch_status_id")
    private StatusBookFootballPitchEntity statusBookFootballPitchEntityBooking;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private PaymentBookingEntity paymentBookingEntityBooking;
}
