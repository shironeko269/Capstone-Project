package com.edu.fud.projectfootbalpitch.repository;

import com.edu.fud.projectfootbalpitch.entity.CategoryProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryProductRepository extends JpaRepository<CategoryProductEntity,Long> {
    List<CategoryProductEntity> findAll();
    @Query(value = "select * from category_products where name=:name",nativeQuery = true)
    Optional<CategoryProductEntity> findAllByName(String name);
    @Query(value = "select name from category_products where id =:categoryId",nativeQuery = true)
    String findNameByCategoryId(long categoryId);
}
