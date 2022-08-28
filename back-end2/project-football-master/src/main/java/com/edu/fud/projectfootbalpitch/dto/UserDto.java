package com.edu.fud.projectfootbalpitch.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends AbstractDto<UserDto> implements Serializable {

    @NotEmpty(message = "Không được bỏ trống!")
    @Pattern(regexp = "^(?=\\S+$).{5,}$",message = "Không được có khoảng trắng!")
    @Pattern(regexp = "^[a-zA-Z0-9&._-]+$",message = "Không có kí tự đặc biệt,UTF-8")
    @Size(min = 5,max = 20,message = "Ít nhất phải 5 kí tự và nhiều nhất 20 kí tự")
    private String userName;

    @NotEmpty(message = "Không được bỏ trống!")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$",
            message = "Phải có 1 chữ hoa,1 chữ thường,1 số, và tối thiểu 8 kí tự")
    @Size(min = 8,message = "Ít nhất phải 8 kí tự")
    private String password;


    @NotEmpty(message = "Không được bỏ trống!")
    @Pattern(regexp = "^[\\p{L}'\\-\\.]+( [\\p{L}'\\-\\.]+)*$",message = "Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách,toàn bộ phải là chữ!")
    @Size(min = 5,max = 30,message = "Ít nhất phải 5 kí tự và nhiều nhất là 30!")
    private String fullName;

    @NotEmpty
    @Pattern(regexp = "((84|0[3|5|7|8|9])+([0-9]{8})\\b)",message = "Số điện thoại không đúng!")
    @Size(min = 10,max = 10,message = "Số điện thoại phải 10 kí tự!")
    private String phone;

    @NotEmpty(message = "Không được bỏ trống!")
    @Email(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)" +
            "*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",message = "Nhập đúng định dạng email!" )
    private String gmail;

    @NotEmpty(message = "Không được bỏ trống!")
    @Pattern(regexp = "^(?=.*[a-z|A-Z].{2,})[\\p{L}'\\-\\.0-9-@!#$%^&*?<>:,;'/\\-_.\\/#&+\\w\\s]+( [\\p{L}'\\-\\.0-9]+)*",
            message = "Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách,Phải có ít nhất 5 chữ cái!")
    @Size(min = 5,max = 40,message = "Ít nhất phải 5 kí tự và nhiều nhất là 40")
    private String address;

    private String employeeNamePitch;

    private String image;

    private Long isMemberId;

    private String role;

    private int orderCount;

    private int bookingCount;
    //hieu
    private int numberUserReturnMost;
    //huy
    private Boolean isEdit = false;
}
