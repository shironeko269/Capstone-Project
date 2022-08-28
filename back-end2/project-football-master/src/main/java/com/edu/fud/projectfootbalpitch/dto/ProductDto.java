package com.edu.fud.projectfootbalpitch.dto;

import com.edu.fud.projectfootbalpitch.config.NumberConstraint;
import com.edu.fud.projectfootbalpitch.entity.CategoryProductEntity;
import com.edu.fud.projectfootbalpitch.entity.SupplierEntity;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto extends AbstractDto<ProductDto> implements Serializable {

    @NotEmpty(message = "Không được bỏ trống")
    @Pattern(regexp = "^(?=.*[a-z|A-Z].{2,})[\\p{L}'\\-\\.0-9-@!#$%^&*?<>:,;'/\\-_.\\/#&+\\w\\s]+( [\\p{L}'\\-\\.0-9]+)*$",
            message = "Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách,Phải có ít nhất 2 chữ cái!")
    @Size(min = 5,max = 500,message = "Ít nhất là 5 và nhiều nhất là 50 kí tự!")
    private String name;

    @NotNull
    @Range(min = 1,message = "Ít nhất là 1 số lớn hơn 0!")
    private int quantity;

    @NotNull
    @Range(min = 1,message = "Ít nhất là 1 số lớn hơn 0!")
    private double price;

    @NotEmpty(message = "Không được bỏ trống")
    @Pattern(regexp = "^(?=.*[a-z|A-Z])[\\p{L}'\\-\\.0-9]+( [\\p{L}'\\-\\.0-9]+)*$",
            message = "Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách,Phải có ít nhất 2 chữ cái!")
    @Size(min = 1,max = 20,message = "Ít nhất là 1 và nhiều nhất là 20 kí tự!")
    private String unit;

    private String image;

    @NotNull
    @Range(min = 1,max = 100,message = "Ít nhất là số lớn hơn 1 và nhiều nhất là 3 số!")
    private double discount;

    @NotEmpty(message = "Không được bỏ trống")
    @Pattern(regexp = "^(?=.*[a-z|A-Z].{5,})[\\p{L}'\\-\\.0-9-@!#$%^&()*~`*?<>:,;'/\\-_.\\/#&+\\w\\s]+( [\\p{L}'\\-\\.0-9]+)*$",
            message = "Chỉ 1 khoảng trắng cách nhau bởi 2 kí tự,Cuối và đầu chuỗi không có khoảng cách,Phải có ít nhất 6 chữ cái!")
    @Size(min = 20,max = 5000,message = "Ít nhất là 20 và nhiều nhất là 500 kí tự!")
    private String description;

    private Long supplierId;

    private String companyName;

    private Long categoryId;

    private String categoryName;

    private int totalQuantityProductSell;
    private double totalPriceProductSell;
    //tao
    private Boolean isEdit = false;
}
