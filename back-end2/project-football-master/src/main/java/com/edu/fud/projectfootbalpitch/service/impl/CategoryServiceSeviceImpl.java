package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.CategoryServiceDto;
import com.edu.fud.projectfootbalpitch.dto.ServiceDto;
import com.edu.fud.projectfootbalpitch.entity.CategoryProductEntity;
import com.edu.fud.projectfootbalpitch.entity.CategoryServiceEntity;
import com.edu.fud.projectfootbalpitch.entity.ServiceEntity;
import com.edu.fud.projectfootbalpitch.repository.CategoryServiceRepository;
import com.edu.fud.projectfootbalpitch.service.CategoryServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceSeviceImpl implements CategoryServiceService{
    @Autowired
    private CategoryServiceRepository categoryServiceRepository;

    @Autowired
    private BeanConfig beanConfig;


    @Override
    public List<CategoryServiceDto> findAll() {
        List<CategoryServiceDto> serviceDtoList = new ArrayList<>();
        List<CategoryServiceEntity> categoryServiceEntities = categoryServiceRepository.findAll();
        for (CategoryServiceEntity categoryServiceEntity :
                categoryServiceEntities){
            serviceDtoList.add(beanConfig.modelMapper().map(categoryServiceEntity,CategoryServiceDto.class));
        }

        return serviceDtoList;
    }

    @Override
    public CategoryServiceDto save(CategoryServiceDto categoryServiceDto) {
        CategoryServiceEntity categoryServiceEntity = new CategoryServiceEntity();
        categoryServiceEntity = beanConfig.modelMapper().map(categoryServiceDto,CategoryServiceEntity.class);
        categoryServiceRepository.save(categoryServiceEntity);
        return beanConfig.modelMapper().map(categoryServiceEntity,CategoryServiceDto.class);
    }

    @Override
    public Optional<CategoryServiceDto> findById(long id) {
        Optional<CategoryServiceEntity> opt =categoryServiceRepository.findById(id);
        return opt.map(categoryServiceEntity -> beanConfig.modelMapper().map(categoryServiceEntity,CategoryServiceDto.class));
    }

    @Override
    public void deleteCategory(long id) {
        categoryServiceRepository.deleteById(id);
    }

    @Override
    public List<CategoryServiceDto> findAllByCategoryName(String categoryName) {
        List<CategoryServiceDto> categoryServiceDtoList = new ArrayList<>();
        List<CategoryServiceEntity> categoryServiceEntityList = categoryServiceRepository.findAllByCategoryName(categoryName);
        for (CategoryServiceEntity categoryServiceEntity : categoryServiceEntityList){
            categoryServiceDtoList.add(beanConfig.modelMapper().map(categoryServiceEntity,CategoryServiceDto.class));
        }
        return categoryServiceDtoList;
    }

}
