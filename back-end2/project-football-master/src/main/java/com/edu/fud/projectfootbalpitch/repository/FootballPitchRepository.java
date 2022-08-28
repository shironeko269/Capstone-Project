package com.edu.fud.projectfootbalpitch.repository;

import com.edu.fud.projectfootbalpitch.entity.FootballPitchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FootballPitchRepository extends JpaRepository<FootballPitchEntity,Long> {
    @Query(value = "select f.* from football_pitchs f where f.ward_id=:WardId",nativeQuery = true)
    List<FootballPitchEntity> findAllByWardAndName(long WardId);

    @Query(value = "select s.* from football_pitchs s where id=(select max(id)from football_pitchs)",nativeQuery = true)
    FootballPitchEntity findByIdMax();

    //@Query(value = "select f.*,u.id,u.fullname from football_pitchs f inner join user u on f.user_id=u.id",nativeQuery = true)
    List<FootballPitchEntity> findAll();

    @Query(value = "select * from football_pitchs where user_id is not null",nativeQuery = true)
    List<FootballPitchEntity> findAllCuaVi();

    @Query(value = "select f.* from football_pitchs f where f.street_number=:streetNumber",nativeQuery = true)
    Optional<FootballPitchEntity> findAllStreetNumber(String streetNumber);

    @Query(value = "select f.* from football_pitchs f where f.urlmap=:urlMap",nativeQuery = true)
    Optional<FootballPitchEntity> findAllUrlMap(String urlMap);

    @Query(value = " select f.street_number from football_pitchs f where f.id=:pitchId",nativeQuery = true)
    String findStreetNumberByPitchId(long pitchId);

    @Query(value = " select f.urlmap from football_pitchs f where f.id=:pitchId",nativeQuery = true)
    String findUrlMapByPitchId(long pitchId);

    @Query(value = "select count(id) from football_pitchs",nativeQuery = true)
    int TotalFootballPitchs();
    @Query(value = "select fl.*,count(book.id) as soLuongDon " +
            "from football_pitchs fl inner join sub_pitch s inner join football_pitchs_schedule fn\n" +
            " inner join book_football_pitch book\n" +
            "on fl.id=s.pitch_pitch_id and s.id=fn.sub_pitch_id where " +
            "fn.id=book.football_pitch_schedule_id group by fl.id \n" +
            "order by count(fl.id) desc limit 5",nativeQuery = true)
    List<FootballPitchEntity> findAllTop5PitchBigBookingMost();
    @Query(value = "select count(book.id) as soLuongDon from football_pitchs fl " +
            "inner join sub_pitch s inner join football_pitchs_schedule fn\n" +
            " inner join book_football_pitch book\n" +
            "on fl.id=s.pitch_pitch_id and s.id=fn.sub_pitch_id and fn.id=book.football_pitch_schedule_id" +
            " where fl.id=:pitchId  group by fl.id \n" +
            "order by count(fl.id) desc;",nativeQuery = true)
    int findCountBookingByFootballPitchBig(long pitchId);

    //hieu
    @Query(value = "select * from football_pitchs where user_id=:userId",nativeQuery = true)
    FootballPitchEntity findFootballPitchEntitiesByUserId(long userId);
    //vi
    @Query("select f from FootballPitchEntity f where f.name like %:name% and f.userEntityPitch.id is not null")
    List<FootballPitchEntity> searchPitchByName(@Param("name")String name);

    @Query( value = "SELECT f.* FROM football_pitchs f inner join wards w on f.ward_id = w.id where w.district_id=:DistrictId and user_id is not null" , nativeQuery = true)
    List<FootballPitchEntity> findPitchByDistrictId(long DistrictId);

    @Query(value = "select f.* from football_pitchs f where f.ward_id=:WardId and user_id is not null",nativeQuery = true)
    List<FootballPitchEntity> findPitchByWardId(long WardId);
    FootballPitchEntity findAllById(long pitchId);
    //duc
    @Query(value = "SELECT * FROM football_pitchs where user_id is not null order by modifieddate DESC limit 8;",nativeQuery = true)
    List<FootballPitchEntity> findLimitByDate();

    //hieu, chatbot
    @Query(value = "SELECT f.* FROM football_pitchs f inner join wards w \n" +
            "on f.ward_id = w.id inner join districts d on w.district_id=d.id \n" +
            "where d.district_name like %:name% or w.ward_name like %:name% " +
            "or f.street_number like %:name% and user_id is not null;",nativeQuery = true)
    List<FootballPitchEntity> findPitchByWardNameOrDistrictName(@Param("name")String name);
}
