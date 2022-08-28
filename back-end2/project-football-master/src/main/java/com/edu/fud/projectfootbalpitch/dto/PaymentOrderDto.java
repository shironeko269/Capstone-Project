package com.edu.fud.projectfootbalpitch.dto;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentOrderDto extends AbstractDto<PaymentOrderDto> implements Serializable {

    private String orderPaymentPaypalId;

    private String amount;

    private String receipt;

    private String status;

    private String payment_paypal_id;

    private Long orderId;

}
