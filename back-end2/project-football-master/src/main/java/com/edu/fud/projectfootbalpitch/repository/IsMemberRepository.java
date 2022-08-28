package com.edu.fud.projectfootbalpitch.repository;

import com.edu.fud.projectfootbalpitch.entity.IsMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IsMemberRepository extends JpaRepository<IsMemberEntity,Long> {
    @Query(value = "select * from is_member where id=1",nativeQuery = true)
    IsMemberEntity findIsNotMember();
}
