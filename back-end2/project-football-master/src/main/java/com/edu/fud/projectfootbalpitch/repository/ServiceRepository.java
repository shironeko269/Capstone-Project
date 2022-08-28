package com.edu.fud.projectfootbalpitch.repository;

import com.edu.fud.projectfootbalpitch.entity.ServiceEntity;
import com.edu.fud.projectfootbalpitch.entity.SubPitchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceRepository extends JpaRepository<ServiceEntity,Long> {
    @Query(value = "select s.*,sum(pss.quantity) as totalQuantitySellMost from services s\n" +
            "inner join pitch_schedule_service as pss\n" +
            "   on pss.service_id = s.id  group by s.id order by totalQuantitySellMost desc limit 5",nativeQuery = true)
    List<ServiceEntity> findAllByQuantitySellMost();

    @Query(value = "select sum(pss.quantity) as totalQuantitySellMost from services s\n" +
            "inner join pitch_schedule_service as pss\n" +
            "   on pss.service_id = s.id where s.id=:serviceId group by s.id",nativeQuery = true)
    int sumQuantitySell(long serviceId);

    @Query(value = "select sum(pss.quantity*s.price) as totalQuantitySellMost from services s\n" +
            "inner join pitch_schedule_service as pss\n" +
            "   on pss.service_id = s.id where s.id=:serviceId group by s.id;",nativeQuery = true)
    double sumPriceSell(long serviceId);
    //hieu
    @Query(value = "select s.*,sum(pss.quantity) as totalQuantitySellMost from services s \n" +
            "inner join (select ser.* from book_football_pitch b inner join football_pitchs_schedule f \n" +
            "inner join pitch_schedule_service ser\n" +
            "inner join sub_pitch sp inner join football_pitchs fp inner join user u \n" +
            "on b.football_pitch_schedule_id=f.id and ser.football_pitch_schedule_id=f.id\n" +
            "and fp.id=sp.pitch_pitch_id and f.sub_pitch_id=sp.id and fp.user_id = u.id \n" +
            "where u.id =:userId limit 5) as pss \n" +
            "on pss.service_id = s.id  group by s.id order by totalQuantitySellMost desc limit 5",nativeQuery = true)
    List<ServiceEntity> findAllServiceByQuantitySellMostByManager(long userId);

    @Query(value = "select sum(pss.quantity) as totalQuantitySellMost from services s \n" +
            "inner join (select ser.* from book_football_pitch b inner join football_pitchs_schedule f \n" +
            "inner join pitch_schedule_service ser\n" +
            "inner join sub_pitch sp inner join football_pitchs fp inner join user u \n" +
            "on b.football_pitch_schedule_id=f.id and ser.football_pitch_schedule_id=f.id\n" +
            "and fp.id=sp.pitch_pitch_id and f.sub_pitch_id=sp.id and fp.user_id = u.id \n" +
            "where u.id =:userId limit 5) as pss \n" +
            "on pss.service_id = s.id where s.id=:serviceId ",nativeQuery = true)
    int sumQuantitySell(long userId,long serviceId);

    @Query(value = "select sum(pss.quantity*s.price) as totalQuantitySellMost from services s \n" +
            "inner join (select ser.* from book_football_pitch b inner join football_pitchs_schedule f \n" +
            "inner join pitch_schedule_service ser\n" +
            "inner join sub_pitch sp inner join football_pitchs fp inner join user u \n" +
            "on b.football_pitch_schedule_id=f.id and ser.football_pitch_schedule_id=f.id\n" +
            "and fp.id=sp.pitch_pitch_id and f.sub_pitch_id=sp.id and fp.user_id = u.id \n" +
            "where u.id =:userId limit 5) as pss on pss.service_id = s.id where s.id=:serviceId",nativeQuery = true)
    double sumPriceSell(long userId,long serviceId);
    //vi
    @Modifying
    @Query(value = "update services set quantity = (quantity-:num) where id = :serviceId", nativeQuery = true)
    void updateQuantity(int num, long serviceId);

    @Query(value = "SELECT * FROM services where category_service_id=:id", nativeQuery = true)
    List<ServiceEntity> findAllByCategoryId(long id);

}
