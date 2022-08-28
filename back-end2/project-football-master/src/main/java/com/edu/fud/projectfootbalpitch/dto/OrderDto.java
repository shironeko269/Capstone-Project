package com.edu.fud.projectfootbalpitch.dto;

import com.edu.fud.projectfootbalpitch.entity.PaymentEntity;
import com.edu.fud.projectfootbalpitch.entity.StatusOrderEntity;
import com.edu.fud.projectfootbalpitch.entity.UserEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto extends AbstractDto<OrderDto> implements Serializable {

    @NotBlank(message = "Không được bỏ trống,Không có khoảng cách!")
    @Size(min = 5,message = "Ít nhất phải 5 kí tự")
    private String userName;

    @NotBlank(message = "Không được bỏ trống,Không có khoảng cách!")
    @Size(min = 5,message = "Ít nhất phải 5 kí tự")
    private String phone;

    private double totalPrice;

    @NotBlank(message = "Không được bỏ trống,Không có khoảng cách!")
    @Size(min = 5,message = "Ít nhất phải 5 kí tự")
    private String address;

    private String note;
    //
    private Long userId;

    private String userNameCreate;

    private String emailUser;
    //
    private Long paymentOrderId;

    private String paymentOrderName;
    //
    private Long statusId;

    private String StatusName;
    //duc
    private String OrderPaymentId;

    private Long[] ProductId;

    private int[] quantity;
}
