package com.edu.fud.projectfootbalpitch.dto;

import com.edu.fud.projectfootbalpitch.entity.OrderEntity;
import com.edu.fud.projectfootbalpitch.entity.ProductsEntity;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto extends AbstractDto<OrderDetailDto> implements Serializable {

    private Long orderId;

    private Long productId;

    private String productName;

    private double discountProduct;

    private int quantity;

    private double price;
    //huy
    private double priceProduct;
    private String unitProduct;
    private Long userID;
    private String address;
    private Long categoryId;
}
