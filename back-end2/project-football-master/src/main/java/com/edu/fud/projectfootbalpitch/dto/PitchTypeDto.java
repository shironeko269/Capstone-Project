package com.edu.fud.projectfootbalpitch.dto;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PitchTypeDto extends AbstractDto<PitchTypeDto> implements Serializable {
    private String code;
    private String name;
}
