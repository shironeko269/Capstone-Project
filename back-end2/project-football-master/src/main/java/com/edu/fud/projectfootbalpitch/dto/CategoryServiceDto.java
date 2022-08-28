package com.edu.fud.projectfootbalpitch.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryServiceDto extends AbstractDto<CategoryServiceDto> implements Serializable {

    @NotNull(message = "Không được bỏ trống!")
    @Pattern( regexp = "^(?=.*[a-z|A-Z].{0,})[\\p{L}'\\-\\.0-9]+( [\\p{L}'\\-\\.0-9]+)*$",
            message = "Chỉ 1 khoảng trắng giữa 2 kí tự, " +
                    "cuối và đầu chuỗi không có dấu khoảng cách, " +
                    "phải có ít nhất 1 chữ cái!")
    @Size(min = 1,message = "Vui lòng nhập tối thiểu 1 kí tự")
    @Size(max = 20,message = "Vui lòng nhập tối đa 20 kí tự")
    private String name;


    private Boolean isEdit = false;

}
