package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.CategoryProductDto;
import com.edu.fud.projectfootbalpitch.entity.CategoryProductEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryProductService {
    List<CategoryProductDto> findAll();
    CategoryProductDto save(CategoryProductDto categoryProductDto);
    Optional<CategoryProductDto> findOne(long categoryId);
    void deleteCategoryById(long id);
    Optional<CategoryProductDto> findAllByName(String name);
    String findNameByCategoryId(long categoryId);
}
