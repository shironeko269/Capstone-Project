package com.edu.fud.projectfootbalpitch.dto;

import lombok.*;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StatusOrderDto extends AbstractDto<StatusOrderDto> implements Serializable {

    private String name;

}
