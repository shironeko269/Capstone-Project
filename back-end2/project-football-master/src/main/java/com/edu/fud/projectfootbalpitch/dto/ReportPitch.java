package com.edu.fud.projectfootbalpitch.dto;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportPitch extends AbstractDto<ReportPitch> implements Serializable {
    private int month;
    private double totalPrice;
}
