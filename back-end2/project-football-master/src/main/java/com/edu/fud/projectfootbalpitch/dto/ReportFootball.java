package com.edu.fud.projectfootbalpitch.dto;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportFootball extends AbstractDto<ReportFootball> implements Serializable {
    private int month;
    private double totalPrice;
}
