package com.edu.fud.projectfootbalpitch.dto;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class    BookingInfo {
    private long subPitchId;
    private double price;
    private Date dateBooking;
    private String timeStart;
    private String timeEnd;
    private double hourFee;
    private List<ServiceDto> listService;
//    private double serviceFee;
    private String note;
    private double preOrder;
    private Long paymentId;


    @Override
    public String toString() {
        return "BookingInfo{" +
                "subPitchId='" + subPitchId + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                ", hourFee='" + hourFee + '\'' +
//                ", serviceFee='" + serviceFee + '\'' +
                ", note='" + note + '\'' +
                ", preOrder='" + preOrder + '\'' +
                '}';
    }
}
