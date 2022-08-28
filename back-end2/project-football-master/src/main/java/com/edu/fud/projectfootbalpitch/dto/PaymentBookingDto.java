package com.edu.fud.projectfootbalpitch.dto;

import com.edu.fud.projectfootbalpitch.entity.BaseEntity;
import com.edu.fud.projectfootbalpitch.entity.BookFootballPitchEntity;
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
public class PaymentBookingDto extends AbstractDto<PaymentBookingDto> implements Serializable {

    private String orderPaymentPaypalId;

    private String amount;

    private String receipt;

    private String status;

    private String payment_paypal_id;

    private Long bookingId;

}
