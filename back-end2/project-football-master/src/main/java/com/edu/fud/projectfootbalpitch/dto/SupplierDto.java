package com.edu.fud.projectfootbalpitch.dto;

import com.edu.fud.projectfootbalpitch.entity.BaseEntity;
import com.edu.fud.projectfootbalpitch.entity.ProductsEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SupplierDto extends AbstractDto<SupplierDto> implements Serializable {

    @NotEmpty(message = "Không được bỏ trống")
    @Pattern(regexp = "^(?=.*[a-z|A-Z].{2,})[\\p{L}'\\-\\.0-9]+( [\\p{L}'\\-\\.0-9]+)*$",
            message = "Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách,Phải có ít nhất 2 chữ cái!")
    @Size(min = 2,max = 50,message = "Ít nhất là 5 và nhiều nhất là 50 kí tự!")
    private String companyName;

    @NotEmpty(message = "Không được bỏ trống")
    @Pattern(regexp = "^(?=.*[a-z|A-Z].{2,})[\\p{L}'\\-\\.0-9-@!#$%^&*?<>:,;'/\\-_.\\/#&+\\w\\s]+( [\\p{L}'\\-\\.0-9]+)*$",
            message = "Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách,Phải có ít nhất 2 chữ cái!")
    @Size(min = 5,max = 50,message = "Ít nhất là 5 và nhiều nhất là 50 kí tự!")
    private String contactName;

    @NotEmpty(message = "Không được bỏ trống!")
    @Pattern(regexp = "^(?=.*[a-z|A-Z].{2,})[\\p{L}'\\-\\.0-9-@!#$%^&*?<>:,;'/\\-_.\\/#&+\\w\\s]+( [\\p{L}'\\-\\.0-9]+)*$",
            message = "Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách,Phải có ít nhất 5 chữ cái!")
    @Size(min = 5,max = 40,message = "Ít nhất phải 5 kí tự và nhiều nhất là 40")
    private String address;

    @NotEmpty(message = "Không được bỏ trống")
    @Pattern(regexp = "((84|0[3|5|7|8|9])+([0-9]{8})\\b)",message = "Số điện thoại không đúng!")
    @Size(min = 10,max = 10,message = "Số điện thoại phải 10 kí tự!")
    private String phone;

}
