package com.edu.fud.projectfootbalpitch.dto;

import lombok.*;

import java.io.Serializable;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DistrictDto extends AbstractDto<DistrictDto> implements Serializable {
    private String districtName;
}
