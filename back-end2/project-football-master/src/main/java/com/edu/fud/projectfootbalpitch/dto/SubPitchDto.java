package com.edu.fud.projectfootbalpitch.dto;

import com.edu.fud.projectfootbalpitch.entity.FootballPitchEntity;
import com.edu.fud.projectfootbalpitch.entity.PitchTypeEntity;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.sql.Time;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubPitchDto extends AbstractDto<SubPitchDto> implements Serializable {

    @NotEmpty(message = "Không được bỏ trống")
    @Pattern(regexp = "^(?=.*[a-z|A-Z].{2,})[\\p{L}'\\-\\.0-9]+( [\\p{L}'\\-\\.0-9]+)*$",
            message = "Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách,Phải có ít nhất 2 chữ cái!")
    @Size(min = 5,max = 50,message = "Ít nhất là 5 và nhiều nhất là 50 kí tự!")
    private String name;

    @NotEmpty(message = "Không được bỏ trống")
    @Pattern(regexp = "^(?=.*[a-z|A-Z].{5,})[\\p{L}'\\-\\.0-9-@!#$%^()*~`&*?<>:,;'/\\-_.\\/#&+\\w\\s]+( [\\p{L}'\\-\\.0-9]+)*$",
            message = "Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách,Phải có ít nhất 6 chữ cái!")
    @Size(min = 20,max = 5000,message = "Ít nhất là 20 và nhiều nhất là 500 kí tự!")
    private String description;

    @NotNull
    @Range(min = 1,message = "Ít nhất là 1 số lớn hơn 0!")
    private double price;

    private String image1;

    private String image2;

    private String image3;

    private String image4;

    private String image5;

    private Long pitchTypeId;

    private String nameType;

    private Long footballPitchId;

    private String footballPitchName;

    //tao
    private Boolean isEdit = false;
    //huy
    private Time subTimeStart;
    private Time subTimeEnd;
    private String Subtype;
}
