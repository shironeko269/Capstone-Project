package com.edu.fud.projectfootbalpitch.repository;

import com.edu.fud.projectfootbalpitch.entity.BookFootballPitchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Time;
import java.util.List;

public interface BookFootballPitchRepository extends JpaRepository<BookFootballPitchEntity,Long> {
    //tao
    @Query(value = "select b.*,f.time_start as GioDatNhieuNhat,count(f.time_start) as SoDonDat from \n" +
            "book_football_pitch b inner join football_pitchs_schedule f on b.football_pitch_schedule_id=f.id \n" +
            "group by f.time_start order by count(f.time_start) desc limit 5",nativeQuery = true)
    List<BookFootballPitchEntity> findAllTimeBookingMost();

    //hieu
    @Query(value = " SELECT * FROM book_football_pitch b inner join booking_payment bp inner join football_pitchs_schedule f\n" +
            " inner join sub_pitch sp inner join football_pitchs fp inner join user u \n" +
            " on b.payment_id= bp.id and b.football_pitch_schedule_id=f.id and fp.id=sp.pitch_pitch_id \n" +
            " and f.sub_pitch_id=sp.id and fp.user_id = u.id where u.id =:userId order by b.id desc",nativeQuery = true)
    List<BookFootballPitchEntity> findAll(long userId);

    @Query(value = "select b.* from book_football_pitch b inner join football_pitchs_schedule f \n" +
            " on b.football_pitch_schedule_id=f.id where b.id=:bookingId",nativeQuery = true)
    BookFootballPitchEntity findByIdViewBookingDetail(long bookingId);

    @Query(value = "select b.*,f.time_start as GioDatNhieuNhat,count(f.time_start) as SoDonDat \n" +
            "from book_football_pitch b inner join football_pitchs_schedule f \n" +
            "inner join sub_pitch sp inner join football_pitchs fp inner join user u\n" +
            "on b.football_pitch_schedule_id=f.id and fp.id=sp.pitch_pitch_id \n" +
            "and f.sub_pitch_id=sp.id and fp.user_id = u.id \n" +
            "where u.id =:userId\n" +
            "group by f.time_start order by count(f.time_start) desc limit 5",nativeQuery = true)
    List<BookFootballPitchEntity> findAllTimeStartByManager(long userId);

    @Query(value = "select SoDonDat from (select b.*,f.time_start \n" +
            "as GioDatNhieuNhat,count(f.time_start) as SoDonDat from book_football_pitch b \n" +
            "inner join football_pitchs_schedule f inner join sub_pitch sp inner join football_pitchs fp \n" +
            "inner join user u on b.football_pitch_schedule_id=f.id and fp.id=sp.pitch_pitch_id \n" +
            "and f.sub_pitch_id=sp.id and fp.user_id = u.id where u.id =:userId group by f.time_start \n" +
            "order by count(f.time_start) desc limit 5) b inner join football_pitchs_schedule f \n" +
            "inner join sub_pitch sp inner join football_pitchs fp inner join user u \n" +
            "on b.football_pitch_schedule_id=f.id and fp.id=sp.pitch_pitch_id and f.sub_pitch_id=sp.id \n" +
            "and fp.user_id = u.id where f.time_start =:time",nativeQuery = true)
    int numberBookingMost(long userId, Time time);

    @Query(value = "select SoDonDat from (select b.*,f.time_start \n" +
            "as GioDatNhieuNhat,count(f.time_start) as SoDonDat from book_football_pitch b \n" +
            "inner join football_pitchs_schedule f inner join sub_pitch sp inner join football_pitchs fp \n" +
            "inner join user u on b.football_pitch_schedule_id=f.id and fp.id=sp.pitch_pitch_id \n" +
            "and f.sub_pitch_id=sp.id and fp.user_id = u.id where u.id =:userId group by f.time_start \n" +
            "order by count(f.time_start) desc limit 5) b inner join football_pitchs_schedule f \n" +
            "inner join sub_pitch sp inner join football_pitchs fp inner join user u \n" +
            "on b.football_pitch_schedule_id=f.id and fp.id=sp.pitch_pitch_id and f.sub_pitch_id=sp.id \n" +
            "and fp.user_id = u.id where f.time_start =:time",nativeQuery = true)
    Integer numberBookingMostCheck(long userId, Time time);

    @Query(value = " select sum(pss.quantity*s.price)  as totalPriceAll from  book_football_pitch b\n" +
            " inner join football_pitchs_schedule f inner join pitch_schedule_service pss \n" +
            " inner join services s inner join sub_pitch sp inner join football_pitchs fp \n" +
            " inner join user u on b.football_pitch_schedule_id = f.id and \n" +
            " pss.football_pitch_schedule_id = f.id and pss.service_id = s.id and f.sub_pitch_id = sp.id\n" +
            " and sp.pitch_pitch_id = fp.id and fp.user_id = u.id\n" +
            " where u.id=:userId",nativeQuery = true)
    double TotalMoneyFromServiceByManagerId(long userId);

    @Query(value = " select sum(pss.quantity*s.price)  as totalPriceAll from  book_football_pitch b\n" +
            " inner join football_pitchs_schedule f inner join pitch_schedule_service pss \n" +
            " inner join services s inner join sub_pitch sp inner join football_pitchs fp \n" +
            " inner join user u on b.football_pitch_schedule_id = f.id and \n" +
            " pss.football_pitch_schedule_id = f.id and pss.service_id = s.id and f.sub_pitch_id = sp.id\n" +
            " and sp.pitch_pitch_id = fp.id and fp.user_id = u.id\n" +
            " where u.id=:userId",nativeQuery = true)
    Double TotalMoneyFromServiceByManagerIdCheck(long userId);

    @Query(value = " select sum(f.price)  as totalPriceAll from  book_football_pitch b\n" +
            " inner join football_pitchs_schedule f inner join sub_pitch sp\n" +
            " inner join football_pitchs fp inner join user u\n" +
            " on b.football_pitch_schedule_id = f.id and f.sub_pitch_id = sp.id\n" +
            " and sp.pitch_pitch_id = fp.id and fp.user_id = u.id\n" +
            " where u.id=:userId",nativeQuery = true)
    double TotalMoneyFromBookingByManagerId(long userId);

    @Query(value = " select sum(f.price)  as totalPriceAll from  book_football_pitch b\n" +
            " inner join football_pitchs_schedule f inner join sub_pitch sp\n" +
            " inner join football_pitchs fp inner join user u\n" +
            " on b.football_pitch_schedule_id = f.id and f.sub_pitch_id = sp.id\n" +
            " and sp.pitch_pitch_id = fp.id and fp.user_id = u.id\n" +
            " where u.id=:userId",nativeQuery = true)
    Double TotalMoneyFromBookingByManagerIdCheck(long userId);

    @Query(value = " select count(f.date_booking) as demdontrongngay from book_football_pitch b\n" +
            " inner join football_pitchs_schedule f inner join sub_pitch sp\n" +
            " inner join football_pitchs fp inner join user u\n" +
            " on b.football_pitch_schedule_id = f.id and month(f.date_booking)=month(curdate())\n" +
            " and f.sub_pitch_id = sp.id and sp.pitch_pitch_id = fp.id and fp.user_id = u.id\n" +
            " where u.id=:userId",nativeQuery = true)
    int TotalBookingByMonthByManagerId(long userId);
    //huy
    @Query(value ="select book.*,f.date_booking as NgayDat   from\n" +
            "            book_football_pitch book inner join booking_payment bp inner join football_pitchs_schedule f \n" +
            "            on book.payment_id = bp.id and book.football_pitch_schedule_id=f.id\n" +
            "            where book.user_id= :userID order by f.id desc",nativeQuery = true)
    List<BookFootballPitchEntity> findAllBookingByUserId(long userID);

    @Query(value ="select f.price as TienSan from\n" +
            "book_football_pitch book inner join football_pitchs_schedule f  \n" +
            " on book.football_pitch_schedule_id=f.id where book.user_id= :userID and book.id=:bookId",nativeQuery = true)
    double findPricePitch(long userID,long bookId);

    @Query(value ="select f.price as TienSan from\n" +
            "book_football_pitch book inner join football_pitchs_schedule f  \n" +
            " on book.football_pitch_schedule_id=f.id where book.user_id= :userID and book.id=:bookId",nativeQuery = true)
    Double findPricePitchCheck(long userID,long bookId);

    @Query(value ="select sum(p.quantity*s.price ) as TienDichVu from\n" +
            "book_football_pitch book inner join booking_payment bp inner join football_pitchs_schedule f inner join\n" +
            "pitch_schedule_service p inner join services s \n" +
            "on book.payment_id = bp.id and book.football_pitch_schedule_id=f.id and \n" +
            "f.id=p.football_pitch_schedule_id and s.id=p.service_id  \n" +
            "where book.user_id=:userID and book.id=:bookId group by book.id",nativeQuery = true)
    double findPriceService(long userID,long bookId);

    @Query(value ="select sum(p.quantity*s.price ) as TienDichVu from\n" +
            "book_football_pitch book inner join booking_payment bp inner join football_pitchs_schedule f inner join\n" +
            "pitch_schedule_service p inner join services s \n" +
            "on book.payment_id = bp.id and book.football_pitch_schedule_id=f.id and \n" +
            "f.id=p.football_pitch_schedule_id and s.id=p.service_id  \n" +
            "where book.user_id=:userID and book.id=:bookId group by book.id",nativeQuery = true)
    Double findPriceServiceCheck(long userID,long bookId);



}
