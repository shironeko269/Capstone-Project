package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.constant.SystemConstant;
import com.edu.fud.projectfootbalpitch.converter.ProductConverter;
import com.edu.fud.projectfootbalpitch.dto.CategoryProductDto;
import com.edu.fud.projectfootbalpitch.dto.FootBallPitchDto;
import com.edu.fud.projectfootbalpitch.dto.ProductDto;
import com.edu.fud.projectfootbalpitch.dto.UserDto;
import com.edu.fud.projectfootbalpitch.entity.*;
import com.edu.fud.projectfootbalpitch.repository.CategoryProductRepository;
import com.edu.fud.projectfootbalpitch.repository.ProductRepository;
import com.edu.fud.projectfootbalpitch.repository.SupplierRepository;
import com.edu.fud.projectfootbalpitch.service.ProductService;
import com.edu.fud.projectfootbalpitch.service.UserService;
import org.modelmapper.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private BeanConfig beanConfig;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private CategoryProductRepository categoryProductRepository;


    @Override
    @Transactional
    public List<ProductDto> findAll() {
        List<ProductDto> productDtoList = new ArrayList<>();
        List<ProductsEntity> productsEntityList = productRepository.findAll();
        for (ProductsEntity entity :
                productsEntityList) {
            ProductDto productDto = beanConfig.modelMapper().map(entity, ProductDto.class);
            productDto.setCategoryName(entity.getCategoryProductEntity().getName());
            productDto.setCompanyName(entity.getSupplierEntity().getCompanyName());
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    //sua
    @Override
    @Transactional
    public ProductDto save(ProductDto productDto) {
        CategoryProductEntity categoryProductEntity =
                categoryProductRepository.getById(productDto.getCategoryId());
        SupplierEntity supplierEntity = supplierRepository.getById(productDto.getSupplierId());
        ProductsEntity productsEntity = new ProductsEntity();
        if (productDto.getId() != null) {
            ProductsEntity oldProductsEntity = productRepository.findById(productDto.getId()).get();
            oldProductsEntity = beanConfig.modelMapper().map(productDto, ProductsEntity.class);
            oldProductsEntity.setCategoryProductEntity(categoryProductEntity);
            oldProductsEntity.setStatus(SystemConstant.ACTIVE_STATUS);
            oldProductsEntity.setSupplierEntity(supplierEntity);
            productRepository.save(oldProductsEntity);
        } else {
            productsEntity = beanConfig.modelMapper().map(productDto, ProductsEntity.class);
//            CategoryProductEntity categoryProductEntity=new CategoryProductEntity();
//            SupplierEntity supplierEntity=new SupplierEntity();
            supplierEntity.setId(productDto.getSupplierId());
            categoryProductEntity.setId(productDto.getCategoryId());
            productsEntity.setCategoryProductEntity(categoryProductEntity);
            productsEntity.setSupplierEntity(supplierEntity);
            productsEntity.setStatus(SystemConstant.ACTIVE_STATUS);
            productRepository.save(productsEntity);
        }

        return beanConfig.modelMapper().map(productsEntity, ProductDto.class);
    }

    //tao
    @Override
    public Optional<ProductDto> findById(long id) {
        Optional<ProductsEntity> opt = productRepository.findById(id);
        return opt.map(productsEntity -> beanConfig.modelMapper().map(productsEntity, ProductDto.class));
    }

    //tao
    @Override
    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDto> findAllPagined(String keyword) {

        List<ProductDto> productDtoList = new ArrayList<>();
        if (keyword == null) {
            keyword = "";
        }
        List<ProductsEntity> productsEntityList = productRepository.findAllPagined(keyword);

        for (ProductsEntity entity :
                productsEntityList) {
            ProductDto productDto = beanConfig.modelMapper().map(entity, ProductDto.class);
            productDtoList.add(productDto);
        }

        return productDtoList;

    }

    @Override
    public List<ProductDto> findLimitByDate() {

        List<ProductDto> productDtoList = new ArrayList<>();
        List<ProductsEntity> productsEntityList = productRepository.findLimitByDate();
        if (productsEntityList != null && productsEntityList.size() > 0) {
            for (ProductsEntity entity :
                    productsEntityList) {
                ProductDto productDto = beanConfig.modelMapper().map(entity, ProductDto.class);
                productDtoList.add(productDto);
            }
        }
        return productDtoList;
    }

    @Override
    public List<ProductDto> findOneByID(long proId) {

        List<ProductDto> productDtoList = new ArrayList<>();
        List<ProductsEntity> productsEntityList = productRepository.findOneByID(proId);
        for (ProductsEntity entity :
                productsEntityList) {
            ProductDto productDto = beanConfig.modelMapper().map(entity, ProductDto.class);
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    @Override
    public List<ProductDto> findAllByCate(long cateId) {
        List<ProductDto> productDtoList = new ArrayList<>();
        List<ProductsEntity> productsEntityList = productRepository.findAllByCate(cateId);
        for (ProductsEntity entity :
                productsEntityList) {
            ProductDto productDto = beanConfig.modelMapper().map(entity, ProductDto.class);
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    @Override
    @Modifying
    @Transactional
    public void updateQuantity(int quantity, long id) {
        productRepository.updateQuantity(quantity, id);
    }

    //chatbot
    @Override
    public List<ProductDto> findAllProductByNameOrCate(String name) {
        List<ProductDto> productDtoList = new ArrayList<>();
        List<ProductsEntity> productsEntityList = productRepository.findAllProductByNameOrCate(name);
        for (ProductsEntity entity :
                productsEntityList) {
            ProductDto productDto = beanConfig.modelMapper().map(entity, ProductDto.class);
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    @Override
    public List<ProductDto> findFiveProductByLastCreateDate() {
        List<ProductDto> productDtoList = new ArrayList<>();
        List<ProductsEntity> productsEntityList = productRepository.findFiveProductByLastCreateDate();
        for (ProductsEntity entity :
                productsEntityList) {
            ProductDto productDto = beanConfig.modelMapper().map(entity, ProductDto.class);
            productDtoList.add(productDto);
        }
        return productDtoList;
    }


}
