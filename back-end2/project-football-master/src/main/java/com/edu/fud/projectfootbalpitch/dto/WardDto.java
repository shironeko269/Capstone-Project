package com.edu.fud.projectfootbalpitch.dto;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WardDto extends AbstractDto<WardDto> implements Serializable {
    private Long districtId;
    private String districtName;
    private String wardName;
}
