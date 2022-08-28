package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.ProductDto;
import com.edu.fud.projectfootbalpitch.entity.ProductsEntity;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<ProductDto> findAll();
    ProductDto save(ProductDto productDto);
    //tao
    Optional<ProductDto> findById(long id);
    void deleteProduct(long id);
    //duc
    List<ProductDto> findLimitByDate();

    List<ProductDto> findAllPagined(String keyword);

    List<ProductDto> findOneByID(long proId);

    List<ProductDto> findAllByCate(long cateId);

    @Modifying
    void updateQuantity(int quatity, long id);

    //chatbot
    List<ProductDto> findAllProductByNameOrCate(String name);
    List<ProductDto> findFiveProductByLastCreateDate();
}
