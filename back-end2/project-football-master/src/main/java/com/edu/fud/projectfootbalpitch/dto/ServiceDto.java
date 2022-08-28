package com.edu.fud.projectfootbalpitch.dto;

import com.edu.fud.projectfootbalpitch.entity.BaseEntity;
import com.edu.fud.projectfootbalpitch.entity.CategoryServiceEntity;
import com.edu.fud.projectfootbalpitch.entity.PitchScheduleServiceEntity;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceDto extends AbstractDto<ServiceDto> implements Serializable {

    @NotNull(message = "Không được bỏ trống!")
    @Pattern(regexp = "^(?=.*[a-z|A-Z].{1,})[\\p{L}'\\-\\.0-9]+( [\\p{L}'\\-\\.0-9]+)*$"
            , message = "Chỉ 1 khoảng trắng giữa 2 kí tự, " +
            "cuối và đầu chuỗi không có dấu khoảng cách, " +
            "phải có ít nhất 1 chữ cái!")
    @Size(min = 1, message = "tối thiểu 1 kí tự")
    @Size(max = 50, message = "tối đa 50 kí tự")
    private String name;

    private String image;

    @NotNull(message = "Không được bỏ trống!")
    @NotBlank(message = "không nhập dấu khoảng cách")
    @Pattern(regexp = "^(?=.*[a-z|A-Z])[\\p{L}'\\-\\.0-9]+( [\\p{L}'\\-\\.0-9]+)*$"
            , message = "Chỉ 1 khoảng trắng giữa 2 kí tự, " +
            "cuối và đầu chuỗi không có dấu khoảng cách, " +
            "phải có ít nhất 1 chữ cái!")
    @Size(min = 1, message = "Ít nhất phải 1 kí tự")
    @Size(max = 10, message = "tối đa 10 kí tự")
    private String unit;

    @NotNull(message = "Không được bỏ trống!")
    @Range(min = 1, message = "Số nhập vào phải lớn hơn 0!")
    private int quantity;

    @NotNull(message = "Không được bỏ trống!")
    @Range(min = 1, message = "Số nhập vào phải lớn hơn 0!")
    private double price;
    private Integer status;
    private Long categoryServiceId;

    private String categoryServiceName;

    private int totalQuantityThongKe;

    private double totalPriceSell;


    private Boolean isEdit = false;
}
