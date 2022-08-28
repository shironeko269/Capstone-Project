package com.edu.fud.projectfootbalpitch.repository;

import com.edu.fud.projectfootbalpitch.entity.FootballPitchScheduleEntity;
import com.edu.fud.projectfootbalpitch.entity.PitchScheduleServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.time.Month;
import java.util.List;
import java.util.Map;

public interface FootballPitchScheduleRepository extends JpaRepository<FootballPitchScheduleEntity,Long> {
    //tao
    @Query(value = "select sum(f.price) from football_pitchs_schedule f",nativeQuery = true)
    double StatisticsPrice();
    @Query(value = "select sum(f.price) from football_pitchs_schedule f",nativeQuery = true)
    Double StatisticsPriceCheck();
    @Query(value = "select month(f.date_booking) month from football_pitchs_schedule f\n" +
            "group by month(f.date_booking) order by month(f.date_booking) asc",nativeQuery = true)
    int[] findAllMonth();
    @Query(value = "select sum(f.price) as soTienThuDuocTrongThang " +
            "from football_pitchs_schedule f where month(f.date_booking)=:month",nativeQuery = true)
    double findAllTotalPriceByMonth(int month);
    @Query(value = "select sum(f.price) as soTienThuDuocTrongThang " +
            "from football_pitchs_schedule f where month(f.date_booking)=:month",nativeQuery = true)
    Double findAllTotalPriceByMonthCheck(int month);

    //hieu
    @Query(value = "select f.id from football_pitchs_schedule f inner join book_football_pitch b\n" +
            " on b.football_pitch_schedule_id=f.id where b.football_pitch_status_id=3"
            ,nativeQuery = true)
    long[] deleteAllByStatusCancel();

    @Query(value = "select month(sche.date_booking) month from user u inner join football_pitchs f inner join sub_pitch sub inner join\n" +
            "football_pitchs_schedule sche on u.id=f.user_id and f.id=sub.pitch_pitch_id and sche.sub_pitch_id\n" +
            "=sub.id where u.id=:userId group by month(sche.date_booking)  order by month(sche.date_booking) asc;",nativeQuery = true)
    int[] findAllMonthOfPitchByManagerId(long userId);

    @Query(value = "select sum(sche.price) from user u inner join football_pitchs f \n" +
            "inner join sub_pitch sub inner join football_pitchs_schedule sche \n" +
            "on u.id=f.user_id and f.id=sub.pitch_pitch_id and sche.sub_pitch_id\n" +
            "=sub.id where u.id=:userId and month(sche.date_booking)=:month",nativeQuery = true)
    double totalPricePitchOfMonthByManagerId(long userId,int month);

    @Query(value = "select sum(sche.price) from user u inner join football_pitchs f \n" +
            "inner join sub_pitch sub inner join football_pitchs_schedule sche \n" +
            "on u.id=f.user_id and f.id=sub.pitch_pitch_id and sche.sub_pitch_id\n" +
            "=sub.id where u.id=:userId and month(sche.date_booking)=:month",nativeQuery = true)
    Double totalPricePitchOfMonthByManagerIdCheck(long userId,int month);

    @Query(value = "select sche.* from user u inner join football_pitchs f \n" +
            "inner join sub_pitch sub inner join football_pitchs_schedule sche \n" +
            "inner join book_football_pitch b \n" +
            "on u.id=f.user_id and f.id=sub.pitch_pitch_id and sche.sub_pitch_id=sub.id \n" +
            "and b.football_pitch_schedule_id = sche.id\n" +
            "where u.id=:userId and current_date() = sche.date_booking \n" +
            "and current_time() <= sche.time_end order by sche.time_end", nativeQuery = true)
    List<FootballPitchScheduleEntity> findAllNow(long userId);

    //vi
    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long findLastInsertId();

    @Query(value = "SELECT f.* FROM football_pitchs_schedule f where date_booking = :dateBooking and sub_pitch_id=:subPitchId  order by time_start", nativeQuery = true)
    List<FootballPitchScheduleEntity> findTimeByDate(Date dateBooking, long subPitchId);
}
