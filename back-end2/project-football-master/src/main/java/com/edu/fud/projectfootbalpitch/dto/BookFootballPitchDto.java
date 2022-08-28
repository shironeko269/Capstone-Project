package com.edu.fud.projectfootbalpitch.dto;

import lombok.*;
import java.sql.Time;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class    BookFootballPitchDto extends AbstractDto<BookFootballPitchDto> implements Serializable {

    @NotBlank(message = "Không được bỏ trống,Không có khoảng cách!")
    private double PreOrderPayment;
    private String note;
    private Long userId;

    private Long footballPitchScheduleId;

    private Long paymentBookingId;
    //tao
    private Time timeBookingMost;
    //statistics
    private String namePitchBookingMost;
    private String streetNumberPitchBookingMost;
    private String wardPitchBookingMost;
    private String districtPitchBookingMost;
    //hieu
    private String subFootballPitchName;
    private String emailUserBooking;
    private String paymentBookingName;

    private Long statusBookFootballPitchId;

    private String statusBookFootballPitchName;
    private String userBook;

    private Date dateCreate;

    private Time timeStart;

    private Time timeEnd;

    private int numberBookingMost;

    private double pricePitchSchedule;
    //huy
    private Date bookingDateCreate;
    private String bookingAmount;
    private String bookingPayment;
    private double totalPitch;
    private double totalService;
    private double totalBooking;
    private String bookingStatus;
    private Long bookingIdStatus;
}
