package com.edu.fud.projectfootbalpitch.repository;

import com.edu.fud.projectfootbalpitch.entity.CategoryServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryServiceRepository extends JpaRepository<CategoryServiceEntity,Long> {
    //hieu
    @Query(value = "select * from category_services cs where cs.name=:categoryName",nativeQuery = true)
    List<CategoryServiceEntity> findAllByCategoryName(String categoryName);
}
