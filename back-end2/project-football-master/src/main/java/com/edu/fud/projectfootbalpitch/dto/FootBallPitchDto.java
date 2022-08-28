package com.edu.fud.projectfootbalpitch.dto;

import com.edu.fud.projectfootbalpitch.entity.UserEntity;
import com.edu.fud.projectfootbalpitch.entity.WardEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FootBallPitchDto extends AbstractDto<FootBallPitchDto> implements Serializable {

    @NotEmpty(message = "Không được bỏ trống")
    @Pattern(regexp = "^(?=.*[a-z|A-Z].{2,})[\\p{L}'\\-\\.0-9/]+( [\\p{L}'\\-\\.0-9]+)*$",
            message = "Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách,Phải có ít nhất 2 chữ cái!")
    @Size(min = 5,max = 50,message = "Ít nhất là 5 và nhiều nhất là 50 kí tự!")
    private String name;

    @NotEmpty(message = "Không được bỏ trống")
    @Pattern(regexp = "^(?=.*[a-z|A-Z].{5,})[\\p{L}'\\-\\.0-9-@!#$()*~`%^&*?<>:,;'/\\-_.\\/#&+\\w\\s]+( [\\p{L}'\\-\\.0-9]+)*$",
            message = "Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách,Phải có ít nhất 6 chữ cái!")
    @Size(min = 20,max = 5000,message = "Ít nhất là 20 và nhiều nhất là 500 kí tự!")
    private String description;

    private String image;

    @NotEmpty(message = "Không được bỏ trống!")
    @Pattern(regexp = "^(?=.*[a-z|A-Z].{5,})[\\p{L}'\\-\\.0-9-@!#$%^&*?<>,;'/\\-_.\\/#&+\\w\\s]+( [\\p{L}'\\-\\.0-9]+)*$",
            message = "Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách,Phải có ít nhất 5 chữ cái!")
    @Size(min = 5,max = 40,message = "Ít nhất phải 5 kí tự và nhiều nhất là 40")
    private String streetNumber;

    @NotBlank(message = "Không được bỏ trống,Không có khoảng cách!")
    @Size(min = 5,max = 2000,message = "Ít nhất phải 5 và nhiều nhất 2000 kí tự!")
    private String urlMap;

    private Long userId;

    private String managerFullName;

    private Long wardId;

    private String wardName;

    private String districtName;
    private int totalBookingPitchBig;
    //tao
    private Boolean isEdit = false;
    //vi
    public String getAddress() {
        return this.streetNumber + " - " + this.wardName + " - " + this.districtName + " - TP Đà Nẵng" ;
    }
}
