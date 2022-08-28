package com.edu.fud.projectfootbalpitch.dto;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PitchScheduleServiceDto extends AbstractDto<PitchScheduleServiceDto> implements Serializable {

    private Long footballPitchScheduleId;

    private Long servicePitchId;

    private int quantity;

    private double price;
    //hieu
    private String nameService;

    private String unit;
    //huy
    private String serName;
    private String serUnit;
    private Double serPrice;

}
