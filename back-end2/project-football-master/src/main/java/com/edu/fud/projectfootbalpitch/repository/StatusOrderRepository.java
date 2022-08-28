package com.edu.fud.projectfootbalpitch.repository;

import com.edu.fud.projectfootbalpitch.entity.OrderEntity;
import com.edu.fud.projectfootbalpitch.entity.StatusOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StatusOrderRepository extends JpaRepository<StatusOrderEntity,Long> {
//    @Modifying
//    @Query(value = "delete from orders s where s.status_order_id=3",nativeQuery = true)
//    void deleteAllByStatusAndIdCancel();

    @Query(value = "select id from orders where status_order_id=3",nativeQuery = true)
    long findAllByIdStatus3();

    @Modifying
    @Query(value = "select * from orders where status_order_id=3",nativeQuery = true)
    long[] deleteAllByStatusAndIdCancel();
    //duc
    @Query(value = "SELECT * FROM order_status where id = 2;",nativeQuery = true)
    StatusOrderEntity findIsNotConfilm();
}
