package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.*;
import com.edu.fud.projectfootbalpitch.entity.BookFootballPitchEntity;
import com.edu.fud.projectfootbalpitch.entity.FootballPitchScheduleEntity;
import com.edu.fud.projectfootbalpitch.entity.ServiceEntity;
import com.edu.fud.projectfootbalpitch.entity.UserEntity;
import com.edu.fud.projectfootbalpitch.repository.*;
import com.edu.fud.projectfootbalpitch.service.Thongke;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThongkeImpl implements Thongke {

    @Autowired
    private BeanConfig beanConfig;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private FootballPitchScheduleRepository footballPitchScheduleRepository;

    @Autowired
    private BookFootballPitchRepository bookFootballPitchRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubPitchRepository subPitchRepository;

    @Autowired
    private PitchScheduleServiceRepository pitchScheduleServiceRepository;

    @Override
    public List<ServiceDto> findAllServiceByQuantitySellMostByManager(long userId) {
        List<ServiceDto> serviceDtoList = new ArrayList<>();
        List<ServiceEntity> serviceEntityList = serviceRepository.findAllServiceByQuantitySellMostByManager(userId);
        for (ServiceEntity entity:
                serviceEntityList){
            ServiceDto serviceDto = beanConfig.modelMapper().map(entity,ServiceDto.class);
            serviceDto.setTotalQuantityThongKe(serviceRepository.sumQuantitySell(userId,entity.getId()));
            serviceDto.setTotalPriceSell(serviceRepository.sumPriceSell(userId,entity.getId()));
            serviceDtoList.add(serviceDto);
        }
        return serviceDtoList;
    }

    @Override
    public List<BookFootballPitchDto> findAllTimeStartByManager(long userId) {
        int totalbookingmost = 0;
        List<BookFootballPitchEntity> bookFootballPitchEntityList =
                bookFootballPitchRepository.findAllTimeStartByManager(userId);
        List<BookFootballPitchDto> bookFootballPitchDtos = new ArrayList<>();
        for (BookFootballPitchEntity entity: bookFootballPitchEntityList ) {
            BookFootballPitchDto bookFootballPitchDto = beanConfig.modelMapper().map(entity,BookFootballPitchDto.class);
            bookFootballPitchDto.setTimeBookingMost(entity.getFootballPitchScheduleEntityBooking().getTimeStart());
            if (bookFootballPitchRepository.numberBookingMostCheck(userId,bookFootballPitchDto.getTimeBookingMost()) == null){
            }else{
                totalbookingmost = bookFootballPitchRepository.numberBookingMost(userId,bookFootballPitchDto.getTimeBookingMost());
            }
            bookFootballPitchDto.setNumberBookingMost(totalbookingmost);
            bookFootballPitchDtos.add(bookFootballPitchDto);
        }
        return bookFootballPitchDtos;
    }

    @Override
    public List<UserDto> findAllUserByReturnByManager(long userId) {
        List<UserEntity> userEntityList =
                userRepository.findAllUserByReturnByManager(userId);
        List<UserDto> userDtos = new ArrayList<>();
        for (UserEntity entity: userEntityList) {
            UserDto userDto = beanConfig.modelMapper().map(entity,UserDto.class);
            userDto.setNumberUserReturnMost(userRepository.numberUserReturnMost(userId
                    ,entity.getUserName()));
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public double TotalMoneyByManager(long userId) {
        double totalMoney=0;
        if (bookFootballPitchRepository.TotalMoneyFromServiceByManagerIdCheck(userId)==null &&
                bookFootballPitchRepository.TotalMoneyFromBookingByManagerIdCheck(userId)==null){

        }else if (bookFootballPitchRepository.TotalMoneyFromServiceByManagerIdCheck(userId)==null||
                bookFootballPitchRepository.TotalMoneyFromBookingByManagerIdCheck(userId)==null){
            totalMoney= bookFootballPitchRepository.TotalMoneyFromBookingByManagerId(userId);
        }else{
            totalMoney =  bookFootballPitchRepository.TotalMoneyFromServiceByManagerId(userId)+
                    bookFootballPitchRepository.TotalMoneyFromBookingByManagerId(userId);
        }
        return totalMoney;
    }

    @Override
    public int TotalBookingByMonthByManagerId(long userId) {
        return bookFootballPitchRepository.TotalBookingByMonthByManagerId(userId);
    }

    @Override
    public int TotalSubPitchByManagerId(long userId) {
        return subPitchRepository.TotalSubPitchByManager(userId);
    }

    @Override
    public List<ReportPitch> findAllTotalPricePitchOfMonthByManagerId(long userId) {
        ReportPitch reportPitch=null;
        int[] arrMonths=new int[12];
        arrMonths[0]=0;
        arrMonths[1]=1;
        arrMonths[2]=2;
        arrMonths[3]=3;
        arrMonths[4]=4;
        arrMonths[5]=5;
        arrMonths[6]=6;
        arrMonths[7]=7;
        arrMonths[8]=8;
        arrMonths[9]=9;
        arrMonths[10]=10;
        arrMonths[11]=11;
        List<ReportPitch> reportPitchList=new ArrayList<>();
        for (int i=1;i<=arrMonths.length;i++){
            reportPitch=new ReportPitch();
            reportPitch.setMonth(i);
            if (footballPitchScheduleRepository.totalPricePitchOfMonthByManagerIdCheck(userId,i)==null){
            }else {
                reportPitch.setTotalPrice(footballPitchScheduleRepository.
                        totalPricePitchOfMonthByManagerId(userId,i));
            }

            reportPitchList.add(reportPitch);
        }
        return reportPitchList;
    }

    @Override
    public List<ReportService> findAllTotalPriceServiceOfMonthByManagerId(long userId) {
        ReportService reportService = null;
        int[] arrMonths=new int[12];
        arrMonths[0]=0;
        arrMonths[1]=1;
        arrMonths[2]=2;
        arrMonths[3]=3;
        arrMonths[4]=4;
        arrMonths[5]=5;
        arrMonths[6]=6;
        arrMonths[7]=7;
        arrMonths[8]=8;
        arrMonths[9]=9;
        arrMonths[10]=10;
        arrMonths[11]=11;
        List<ReportService> reportServiceList = new ArrayList<>();
        for (int i=1;i<=arrMonths.length;i++){
            reportService = new ReportService();
            reportService.setMonth(i);
            if (pitchScheduleServiceRepository.totalPriceServiceOfMonthByManagerIdCheck(userId,i)==null){
            }else {
                reportService.setTotalPrice(
                        pitchScheduleServiceRepository.totalPriceServiceOfMonthByManagerId(userId,i));
            }
            reportServiceList.add(reportService);
        }
        return reportServiceList;
    }

}