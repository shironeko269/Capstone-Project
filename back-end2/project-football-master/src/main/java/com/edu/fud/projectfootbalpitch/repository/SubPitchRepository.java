package com.edu.fud.projectfootbalpitch.repository;

import com.edu.fud.projectfootbalpitch.entity.SubPitchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface SubPitchRepository extends JpaRepository<SubPitchEntity , Long> {
    @Query(value = "select s.* from football_pitchs f inner join sub_pitch s on f.id=s.pitch_pitch_id where f.id=(select max(id)from football_pitchs)",nativeQuery = true)
    List<SubPitchEntity> findAllByFootballPitchEntitySub();

    @Query(value = "select * from sub_pitch where pitch_pitch_id=:idPitchBig",nativeQuery = true)
    List<SubPitchEntity> findAndByIdPitchBig(long idPitchBig);
    //hieu
    @Query(value = "select s.* from football_pitchs f inner join sub_pitch s on f.id=s.pitch_pitch_id where f.user_id=:userId",nativeQuery = true)
    List<SubPitchEntity> findAllSubPitchByUserId(long userId);

    @Query(value = " select count(sp.id) as tongsanbong from sub_pitch sp inner join\n" +
            " football_pitchs fp inner join user u \n" +
            " on sp.pitch_pitch_id = fp.id and fp.user_id = u.id\n" +
            " where u.id=:userId",nativeQuery = true)
    int TotalSubPitchByManager(long userId);
    //vi
    @Query(value = "select * from sub_pitch where pitch_pitch_id=:id", nativeQuery = true)
    List<SubPitchEntity> findAllByPitchId(long id);

    //@Query(value = "select * from sub_pitch where id=:id", nativeQuery = true)
    //SubPitchEntity findById(long id);

    @Query(value = "select * from sub_pitch where pitch_pitch_id=:pitchId and pitch_type_id=:typeId", nativeQuery = true)
    List<SubPitchEntity> findAllByPitchTypeId(long pitchId, long typeId);
    //huy
    @Query(value ="select sub.* from user u inner join book_football_pitch book inner join football_pitchs_schedule sche inner join sub_pitch sub on \n" +
            "            book.football_pitch_schedule_id=sche.id\n" +
            "and sche.sub_pitch_id=sub.id where book.football_pitch_schedule_id=:shedulePitchID and u.id=:userID",nativeQuery = true)
    List<SubPitchEntity> findAllBySubPitch(long shedulePitchID,long userID);
    @Query(value ="select sche.time_start from user u inner join book_football_pitch book inner join football_pitchs_schedule sche inner join sub_pitch sub on \n" +
            "            book.football_pitch_schedule_id=sche.id\n" +
            "and sche.sub_pitch_id=sub.id where book.football_pitch_schedule_id=:shedulePitchID and u.id=:userID",nativeQuery = true)
    Time findAllBySubPitchStartTime(long shedulePitchID, long userID);
    @Query(value ="select sche.time_end from user u inner join book_football_pitch book inner join football_pitchs_schedule sche inner join sub_pitch sub on \n" +
            "            book.football_pitch_schedule_id=sche.id\n" +
            "and sche.sub_pitch_id=sub.id where book.football_pitch_schedule_id=:shedulePitchID and u.id=:userID",nativeQuery = true)
    Time findAllBySubPitchEndTime(long shedulePitchID,long userID);

    //chatbot
    @Query(value = "select * from sub_pitch s \n" +
            "where s.id not in (select sp.id as asd from sub_pitch sp\n" +
            "inner join football_pitchs_schedule fps on fps.sub_pitch_id = sp.id\n" +
            "where ( fps.date_booking =:dateBooking and \n" +
            ":timeBooking between fps.time_start and fps.time_end )) \n" +
            "or not exists (select * from football_pitchs_schedule fps\n" +
            "where fps.sub_pitch_id = s.id );", nativeQuery = true)
    List<SubPitchEntity> findSubPitchByDateAndTime(Date dateBooking, Time timeBooking);

    @Query(value = "select * from sub_pitch s \n" +
            "inner join football_pitchs f on f.id = s.pitch_pitch_id\n" +
            "inner join wards w on f.ward_id = w.id \n" +
            "inner join districts d on w.district_id=d.id \n" +
            "where \n" +
            "((d.district_name like %:keyword% or w.ward_name like %:keyword% \n" +
            "or f.street_number like %:keyword%) and f.user_id is not null) \n" +
            "and\n" +
            "(s.id not in (select sp.id as asd from sub_pitch sp\n" +
            "inner join football_pitchs_schedule fps on fps.sub_pitch_id = sp.id\n" +
            "where ( fps.date_booking = :dateBooking and \n" +
            ":timeBooking between fps.time_start and fps.time_end )) \n" +
            "or not exists (select * from football_pitchs_schedule fps\n" +
            "where fps.sub_pitch_id = s.id ));", nativeQuery = true)
    List<SubPitchEntity> findSubPitchByAreasAndSpecTime(String keyword,Date dateBooking, Time timeBooking);
}
