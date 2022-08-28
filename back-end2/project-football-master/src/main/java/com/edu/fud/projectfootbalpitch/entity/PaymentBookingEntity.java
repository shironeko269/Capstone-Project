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
@Table(name = "booking_payment")
public class PaymentBookingEntity extends BaseEntity implements Serializable {

    private String orderPaymentPaypalId;

    private String amount;

    private String receipt;

    private String status;

    private String payment_paypal_id;

    @OneToOne(mappedBy = "paymentBookingEntityBooking")
    private BookFootballPitchEntity book_id;

}
