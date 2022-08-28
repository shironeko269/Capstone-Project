package com.edu.fud.projectfootbalpitch.service.impl;
import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.BookFootballPitchDto;
import com.edu.fud.projectfootbalpitch.dto.ServiceDto;
import com.edu.fud.projectfootbalpitch.entity.*;
import com.edu.fud.projectfootbalpitch.repository.*;
import com.edu.fud.projectfootbalpitch.service.BookFootballPitchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookFootballPitchServiceImpl implements BookFootballPitchService{
    @Autowired
    private BookFootballPitchRepository bookFootballPitchRepository;

    @Autowired
    private BeanConfig beanConfig;

    @Autowired
    private StatusBookFootballPitchRepository statusBookFootballPitchRepository;

    @Autowired
    private PaymentBookFootballPitchRepository paymentBookFootballPitchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FootballPitchScheduleRepository footballPitchScheduleRepository;

    @Override
    public List<BookFootballPitchDto> findAll(long userId) {
        List<BookFootballPitchDto> bookFootballPitchDtos = new ArrayList<>();
        List<BookFootballPitchEntity> bookFootballPitchEntities = bookFootballPitchRepository.findAll(userId);
        for (BookFootballPitchEntity entity: bookFootballPitchEntities){
            BookFootballPitchDto bookFootballPitchDto = beanConfig.modelMapper().map(entity,BookFootballPitchDto.class);
            bookFootballPitchDto.setStatusBookFootballPitchName(entity.getStatusBookFootballPitchEntityBooking().getName());
            bookFootballPitchDto.setPaymentBookingName(entity.getPaymentBookingEntityBooking().getStatus());
            bookFootballPitchDto.setSubFootballPitchName(entity.getFootballPitchScheduleEntityBooking().getSubPitchEntitySchedule().getName());
            bookFootballPitchDto.setUserBook(entity.getUserEntityPitchBooking().getUserName());
            //    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            bookFootballPitchDto.setDateCreate(entity.getFootballPitchScheduleEntityBooking().getDateBooking());
            bookFootballPitchDto.setTimeStart(entity.getFootballPitchScheduleEntityBooking().getTimeStart());
            bookFootballPitchDto.setTimeEnd(entity.getFootballPitchScheduleEntityBooking().getTimeEnd());
            bookFootballPitchDto.setEmailUserBooking(entity.getUserEntityPitchBooking().getGmail());
            bookFootballPitchDtos.add(bookFootballPitchDto);
        }
        return bookFootballPitchDtos;
    }

    @Override
    public BookFootballPitchDto save(BookFootballPitchDto bookFootballPitchDto) {
        PaymentBookingEntity paymentBookingEntity = paymentBookFootballPitchRepository.getById(bookFootballPitchDto.getPaymentBookingId());
        StatusBookFootballPitchEntity statusBookFootballPitchEntity = statusBookFootballPitchRepository.getById(bookFootballPitchDto.getStatusBookFootballPitchId());
        FootballPitchScheduleEntity footballPitchScheduleEntity = footballPitchScheduleRepository.getById(bookFootballPitchDto.getFootballPitchScheduleId());
        UserEntity userEntity=userRepository.getById(bookFootballPitchDto.getUserId());
        BookFootballPitchEntity oldBookFootballPitchEntity = bookFootballPitchRepository.getById(bookFootballPitchDto.getId());
        oldBookFootballPitchEntity = beanConfig.modelMapper().map(bookFootballPitchDto, BookFootballPitchEntity.class);
        oldBookFootballPitchEntity.setPaymentBookingEntityBooking(paymentBookingEntity);
        oldBookFootballPitchEntity.setStatusBookFootballPitchEntityBooking(statusBookFootballPitchEntity);
        oldBookFootballPitchEntity.setUserEntityPitchBooking(userEntity);
        oldBookFootballPitchEntity.setFootballPitchScheduleEntityBooking(footballPitchScheduleEntity);
        bookFootballPitchRepository.save(oldBookFootballPitchEntity);
        return beanConfig.modelMapper().map(oldBookFootballPitchEntity,BookFootballPitchDto.class);
    }

    @Override
    public BookFootballPitchDto findByIdViewBookingDetail(long bookingId) {
        BookFootballPitchEntity bookFootballPitchEntity=
                bookFootballPitchRepository.findByIdViewBookingDetail(bookingId);
        BookFootballPitchDto bookFootballPitchDto=beanConfig.modelMapper().map(bookFootballPitchEntity,BookFootballPitchDto.class);
        bookFootballPitchDto.setDateCreate(bookFootballPitchEntity.getFootballPitchScheduleEntityBooking().getDateBooking());
        bookFootballPitchDto.setTimeStart(bookFootballPitchEntity.getFootballPitchScheduleEntityBooking().getTimeStart());
        bookFootballPitchDto.setPaymentBookingName(bookFootballPitchEntity.getPaymentBookingEntityBooking().getStatus());
        bookFootballPitchDto.setTimeEnd(bookFootballPitchEntity.getFootballPitchScheduleEntityBooking().getTimeEnd());
        bookFootballPitchDto.setPricePitchSchedule(bookFootballPitchEntity.getFootballPitchScheduleEntityBooking().getPrice());
        return bookFootballPitchDto;
    }

    @Override
    public void deleteBooking(long id) {
        footballPitchScheduleRepository.deleteById(id);
    }

    @Override
    public void deleteAllByStatusCancel() {
        long[] bookingEntity= footballPitchScheduleRepository.deleteAllByStatusCancel();
        for(long id:bookingEntity){
            footballPitchScheduleRepository.deleteById(id);
        }
    }

    @Override
    public Long saveOfVi(BookFootballPitchDto dto) {
        BookFootballPitchEntity entity = new BookFootballPitchEntity();
        entity = beanConfig.modelMapper().map(dto, BookFootballPitchEntity.class);

        FootballPitchScheduleEntity footballPitchScheduleEntity = new FootballPitchScheduleEntity();
        footballPitchScheduleEntity.setId(dto.getFootballPitchScheduleId());
        entity.setFootballPitchScheduleEntityBooking(footballPitchScheduleEntity);

        PaymentBookingEntity paymentBookingEntity = new PaymentBookingEntity();
        paymentBookingEntity.setId(dto.getPaymentBookingId());
        entity.setPaymentBookingEntityBooking(paymentBookingEntity);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(dto.getUserId());
        entity.setUserEntityPitchBooking(userEntity);

        StatusBookFootballPitchEntity statusBookFootballPitchEntity = new StatusBookFootballPitchEntity();
        statusBookFootballPitchEntity.setId(dto.getStatusBookFootballPitchId());
        entity.setStatusBookFootballPitchEntityBooking(statusBookFootballPitchEntity);

        bookFootballPitchRepository.save(entity);
        return entity.getId();
    }

    @Override
    public List<BookFootballPitchDto> findAllByUserID(long userID){
        double totalPitch = 0,totalService=0,totalbook=0;
        List<BookFootballPitchDto> bookFootballPitchDtoList = new ArrayList<>();
        List<BookFootballPitchEntity> bookFootballPitchEntityList = bookFootballPitchRepository.findAllBookingByUserId(userID);
        for(BookFootballPitchEntity entity:bookFootballPitchEntityList){
            BookFootballPitchDto bookFootballPitchDto = beanConfig.modelMapper().map(entity,BookFootballPitchDto.class);
            if (bookFootballPitchRepository.findPricePitchCheck(userID,entity.getId()) == null &&
                bookFootballPitchRepository.findPriceServiceCheck(userID,entity.getId())== null) {
                totalPitch = 0;
                totalService = 0;
                totalbook =0;
            }else if(bookFootballPitchRepository.findPricePitchCheck(userID,entity.getId()) == null &&
                    bookFootballPitchRepository.findPriceServiceCheck(userID,entity.getId())!= null){
                totalPitch = 0;
                totalService = totalbook = bookFootballPitchRepository.findPriceService(userID,entity.getId());
            } else if (bookFootballPitchRepository.findPricePitchCheck(userID,entity.getId()) != null &&
                    bookFootballPitchRepository.findPriceServiceCheck(userID,entity.getId())== null) {
                totalService = 0;
                totalPitch = totalbook = bookFootballPitchRepository.findPricePitch(userID,entity.getId());
            }else{
                totalPitch = bookFootballPitchRepository.findPricePitch(userID,entity.getId());
                totalService = bookFootballPitchRepository.findPriceService(userID,entity.getId());
                totalbook = totalPitch + totalService;
            }
            bookFootballPitchDto.setTotalPitch(totalPitch);
            bookFootballPitchDto.setTotalService(totalService);
            bookFootballPitchDto.setTotalBooking(totalbook);
            bookFootballPitchDto.setBookingDateCreate(entity.getFootballPitchScheduleEntityBooking().getDateBooking());
            bookFootballPitchDto.setBookingAmount(entity.getPaymentBookingEntityBooking().getAmount());
            bookFootballPitchDto.setBookingPayment(entity.getPaymentBookingEntityBooking().getStatus());
            bookFootballPitchDto.setBookingStatus(entity.getStatusBookFootballPitchEntityBooking().getName());
            bookFootballPitchDto.setBookingIdStatus(entity.getStatusBookFootballPitchEntityBooking().getId());
            bookFootballPitchDtoList.add(bookFootballPitchDto);
        }
        return  bookFootballPitchDtoList;
    }
}
