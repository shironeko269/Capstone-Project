package com.edu.fud.projectfootbalpitch.dto;

import lombok.*;

import javax.persistence.Column;
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
public class CategoryProductDto extends AbstractDto<CategoryProductDto> implements Serializable {

    @NotEmpty(message = "Không được bỏ trống!")
    @Pattern(regexp = "^(?=.*[a-z|A-Z].{2,})[\\p{L}'\\-\\.0-9-@!#$%^&*?<>:,;'/\\-_.\\/#&+\\w\\s]+( [\\p{L}'\\-\\.0-9]+)*$",
            message = "Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách,Phải có ít nhất 5 chữ cái!")
    @Size(min = 1,max = 500,message = "Ít nhất phải 5 kí tự và nhiều nhất là 30")
    private String name;

}
