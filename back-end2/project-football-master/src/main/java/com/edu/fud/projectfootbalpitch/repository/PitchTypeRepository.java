package com.edu.fud.projectfootbalpitch.repository;

import com.edu.fud.projectfootbalpitch.entity.PitchTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PitchTypeRepository extends JpaRepository<PitchTypeEntity,Long> {
    //vi
    @Query(value = "SELECT * FROM pitch_type where id=:typeId", nativeQuery = true)
    PitchTypeEntity findPitchTypeById(long typeId);
}
