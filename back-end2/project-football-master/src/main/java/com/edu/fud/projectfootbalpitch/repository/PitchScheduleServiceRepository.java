package com.edu.fud.projectfootbalpitch.repository;

import com.edu.fud.projectfootbalpitch.entity.PitchScheduleServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface PitchScheduleServiceRepository extends JpaRepository<PitchScheduleServiceEntity,Long> {
    //tao
    @Query(value = " select sum(price*quantity) from pitch_schedule_service",nativeQuery = true)
    double StatisticsPrice();
    @Query(value = " select sum(price*quantity) from pitch_schedule_service",nativeQuery = true)
    Double StatisticsPriceCheck();
    @Query(value = "select month(p.createddate) month  from services s\n" +
            " inner join pitch_schedule_service p on s.id=p.service_id\n" +
            "group by month(p.createddate) order by  month(p.createddate) asc",nativeQuery = true)
    int[] findAllMonthOfService();
    @Query(value = "select sum(s.price*p.quantity) as soTienThuDuocTrongThang from services s\n" +
            " inner join pitch_schedule_service p on s.id=p.service_id where month(p.createddate)=:month",nativeQuery = true)
    double totalPriceOfServiceByMonth(int month);
    @Query(value = "select sum(s.price*p.quantity) as soTienThuDuocTrongThang from services s\n" +
            " inner join pitch_schedule_service p on s.id=p.service_id where month(p.createddate)=:month",nativeQuery = true)
    Double totalPriceOfServiceByMonthDouble(int month);
    //hieu
    @Query(value = "select s.* from book_football_pitch b inner join football_pitchs_schedule f inner join \n" +
            " pitch_schedule_service s on f.id = s.football_pitch_schedule_id  and \n" +
            " f.id=b.football_pitch_schedule_id where b.id=:bookingId",nativeQuery = true)
    List<PitchScheduleServiceEntity> findAllServiceByPitchScheduleByBooking(long bookingId);

    @Query(value = "select month(pser.createddate) month from user u inner join \n" +
            "football_pitchs f inner join sub_pitch sub inner join football_pitchs_schedule sche \n" +
            "inner join pitch_schedule_service pser inner join services s\n" +
            "on u.id=f.user_id and f.id=sub.pitch_pitch_id and sche.sub_pitch_id\n" +
            "=sub.id and pser.football_pitch_schedule_id = sche.id and pser.service_id = s.id\n" +
            "where u.id=:userId group by month(pser.createddate)  order by month(pser.createddate) asc",nativeQuery = true)
    int[] findAllMonthOfServiceByManagerId(long userId);

    @Query(value = "select sum(pser.quantity*s.price) from user u inner join \n" +
            "football_pitchs f inner join sub_pitch sub inner join football_pitchs_schedule sche \n" +
            "inner join pitch_schedule_service pser inner join services s\n" +
            "on u.id=f.user_id and f.id=sub.pitch_pitch_id and sche.sub_pitch_id\n" +
            "=sub.id and pser.football_pitch_schedule_id = sche.id and pser.service_id = s.id\n" +
            "where u.id=:userId and month(sche.date_booking)=:month ;",nativeQuery = true)
    double totalPriceServiceOfMonthByManagerId(long userId,int month);
    @Query(value = "select sum(pser.quantity*s.price) from user u inner join \n" +
            "football_pitchs f inner join sub_pitch sub inner join football_pitchs_schedule sche \n" +
            "inner join pitch_schedule_service pser inner join services s\n" +
            "on u.id=f.user_id and f.id=sub.pitch_pitch_id and sche.sub_pitch_id\n" +
            "=sub.id and pser.football_pitch_schedule_id = sche.id and pser.service_id = s.id\n" +
            "where u.id=:userId and month(sche.date_booking)=:month ;",nativeQuery = true)
    Double totalPriceServiceOfMonthByManagerIdCheck(long userId,int month);
    @Query(value = "select * from pitch_schedule_service  where football_pitch_schedule_id=:id ;",nativeQuery = true)
    List<PitchScheduleServiceEntity> findAllByScheduleId(Long id);

    @Transactional
    @Query(value ="update pitch_schedule_service set quantity =:quantity where football_pitch_schedule_id =:scheduleId and service_id=:serviceId",nativeQuery = true )
    void UpdatePitchServiceQuantity(int quantity,long scheduleId,long serviceId);

    @Query(value = "select pss.id from  pitch_schedule_service pss where pss.football_pitch_schedule_id =:scheduleId and pss.service_id=:serviceId",nativeQuery = true)
    Long pitchScheduleServiceId(long scheduleId,long serviceId);

    @Query(value = "select * from  pitch_schedule_service pss where pss.football_pitch_schedule_id =:scheduleId and pss.service_id=:serviceId",nativeQuery = true)
    PitchScheduleServiceEntity findOnePitchScheduleServiceByScheduleIdAndServiceId(long scheduleId,long serviceId);

    @Query(value = "select sum(pss.price*pss.quantity) from pitch_schedule_service pss where pss.football_pitch_schedule_id=:scheduleId",nativeQuery = true)
    double sumOldScheduleService(long scheduleId);
    @Query(value = "select sum(pss.price*pss.quantity) from pitch_schedule_service pss where pss.football_pitch_schedule_id=:scheduleId",nativeQuery = true)
    Double sumOldScheduleServiceCheck(long scheduleId);
    //huy
    @Query(value ="select pss.*,s.name,s.unit, s.price,pss.quantity from pitch_schedule_service as pss \n" +
            "inner join services as s  inner join user as u\n" +
            "on pss.service_id = s.id\n" +
            "where pss.football_pitch_schedule_id = :id1 and u.id = :userID",nativeQuery = true)
    List<PitchScheduleServiceEntity> findAllByScheduleService(long id1,long userID);
}
