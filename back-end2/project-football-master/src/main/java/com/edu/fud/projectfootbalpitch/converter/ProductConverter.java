package com.edu.fud.projectfootbalpitch.converter;

import com.edu.fud.projectfootbalpitch.dto.ProductDto;
import com.edu.fud.projectfootbalpitch.entity.ProductsEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    public ProductDto toDto(ProductsEntity productsEntity){
        ProductDto productDto=new ProductDto();
        productDto.setId(productsEntity.getId());
        productDto.setName(productsEntity.getName());
        productDto.setQuantity(productsEntity.getQuantity());
        productDto.setPrice(productsEntity.getPrice());
        productDto.setUnit(productsEntity.getUnit());
        productDto.setImage(productsEntity.getImage());
        productDto.setDiscount(productsEntity.getDiscount());
        productDto.setDescription(productsEntity.getDescription());
        productDto.setCompanyName(productsEntity.getSupplierEntity().getCompanyName());
        productDto.setCategoryName(productsEntity.getCategoryProductEntity().getName());
        return productDto;
    }
}
