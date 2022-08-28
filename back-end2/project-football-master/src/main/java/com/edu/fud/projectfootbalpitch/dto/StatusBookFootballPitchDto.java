package com.edu.fud.projectfootbalpitch.dto;

import lombok.*;


import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StatusBookFootballPitchDto extends AbstractDto<StatusBookFootballPitchDto> implements Serializable {
    private String name;

}
