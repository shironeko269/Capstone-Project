package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.CategoryProductDto;
import com.edu.fud.projectfootbalpitch.dto.ProductDto;
import com.edu.fud.projectfootbalpitch.entity.CategoryProductEntity;
import com.edu.fud.projectfootbalpitch.entity.ProductsEntity;
import com.edu.fud.projectfootbalpitch.repository.CategoryProductRepository;
import com.edu.fud.projectfootbalpitch.service.CategoryProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryProductServiceImpl implements CategoryProductService {

    @Autowired
    private CategoryProductRepository categoryProductRepository;

    @Autowired
    private BeanConfig beanConfig;

    @Override
    public List<CategoryProductDto> findAll() {
        List<CategoryProductDto> productDtoList=new ArrayList<>();
        List<CategoryProductEntity> categoryProductEntities= categoryProductRepository.findAll();
        for (CategoryProductEntity categoryProductEntity :
                categoryProductEntities) {
            productDtoList.add(beanConfig.modelMapper().map(categoryProductEntity,CategoryProductDto.class));
        }
        return productDtoList;
    }

    @Override
    public CategoryProductDto save(CategoryProductDto categoryProductDto) {
        CategoryProductEntity categoryProductEntity=new CategoryProductEntity();
        categoryProductEntity=beanConfig.modelMapper().map(categoryProductDto,CategoryProductEntity.class);
        categoryProductRepository.save(categoryProductEntity);
        return beanConfig.modelMapper().map(categoryProductEntity,CategoryProductDto.class);
    }

    @Override
    public Optional<CategoryProductDto> findOne(long categoryId) {
        Optional<CategoryProductEntity> optional=categoryProductRepository.findById(categoryId);
        return optional.map(categoryProductEntity -> beanConfig.modelMapper().map(categoryProductEntity,CategoryProductDto.class));
    }

    @Override
    public void deleteCategoryById(long id) {
        categoryProductRepository.deleteById(id);
    }


    @Override
    public Optional<CategoryProductDto> findAllByName(String name) {
        Optional<CategoryProductEntity> optional=categoryProductRepository.findAllByName(name);
        return optional.map(categoryProductEntity ->
                beanConfig.modelMapper().map(categoryProductEntity,CategoryProductDto.class));
    }

    @Override
    public String findNameByCategoryId(long categoryId) {
        String nameByCategoryId=categoryProductRepository.findNameByCategoryId(categoryId);
        return nameByCategoryId;
    }
}
