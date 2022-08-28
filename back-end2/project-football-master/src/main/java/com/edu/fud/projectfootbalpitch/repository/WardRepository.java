package com.edu.fud.projectfootbalpitch.repository;

import com.edu.fud.projectfootbalpitch.entity.WardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WardRepository extends JpaRepository<WardEntity,Long> {

    @Query(value = "select * from wards where district_id=:distinctId",nativeQuery = true)
    List<WardEntity> findAllByDistrictId(long distinctId);

    @Query(value = "select * from wards where id=:wardId",nativeQuery = true)
    WardEntity findById(long wardId);


}
