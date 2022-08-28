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
@Table(name = "orders")
public class OrderEntity extends BaseEntity implements Serializable {

    @Column(name = "username")
    private String userName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "address")
    private String address;

    @Column(name = "note",columnDefinition = "nvarchar(500) not null")
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntityOrder;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private PaymentEntity paymentOrderEntity;

    @ManyToOne
    @JoinColumn(name = "status_order_id")
    private StatusOrderEntity statusOrderEntity;

    @OneToMany(mappedBy = "orderEntityOrderDetail",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<OrderDetailEntity> orderDetailEntities = new ArrayList<>();
}
