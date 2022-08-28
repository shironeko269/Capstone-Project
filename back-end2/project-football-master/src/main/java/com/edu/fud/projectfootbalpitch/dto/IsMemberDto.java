package com.edu.fud.projectfootbalpitch.dto;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IsMemberDto extends AbstractDto<IsMemberDto> implements Serializable {

    private String name;

}
