package com.edu.fud.projectfootbalpitch.service.impl;

import com.edu.fud.projectfootbalpitch.config.BeanConfig;
import com.edu.fud.projectfootbalpitch.dto.*;
import com.edu.fud.projectfootbalpitch.entity.*;
import com.edu.fud.projectfootbalpitch.repository.*;
import com.edu.fud.projectfootbalpitch.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private PitchScheduleServiceRepository pitchScheduleServiceRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private FootballPitchScheduleRepository footballPitchScheduleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FootballPitchRepository footballPitchRepository;
    @Autowired
    private BookFootballPitchRepository bookFootballPitchRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private BeanConfig beanConfig;

    @Override
    public double statisticsSystem() {
        double servicePitch = 0,totalPriceOrder = 0,pitchPriceSchedule = 0;
        if (pitchScheduleServiceRepository.StatisticsPriceCheck() != null){
            servicePitch = pitchScheduleServiceRepository.StatisticsPrice();
        }
        if (ordersRepository.StatisticsTotalPriceCheck() != null) {
            totalPriceOrder = ordersRepository.StatisticsTotalPrice();
        }
        if (footballPitchScheduleRepository.StatisticsPriceCheck() != null) {
            pitchPriceSchedule = footballPitchScheduleRepository.StatisticsPrice();
        }
        return servicePitch + totalPriceOrder + pitchPriceSchedule;
    }

    @Override
    public int totalProducts() {
        return productRepository.TotalProduct();
    }

    @Override
    public int totalUsers() {
        return userRepository.TotalUsers();
    }

    @Override
    public int totalFootballPitch() {
        return footballPitchRepository.TotalFootballPitchs();
    }

    @Override
    public List<UserDto> findAllBookingLimit5ByUser() {
        List<UserDto> userDtoList = new ArrayList<>();
        List<UserEntity> userEntityList = userRepository.findAllBookingLimit5ByUser();
        for (UserEntity userEntity : userEntityList
        ) {
            UserDto userDto = beanConfig.modelMapper().map(userEntity, UserDto.class);
            userDto.setBookingCount(userEntity.getBookFootballPitchEntitiesUser().size());
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    @Override
    public List<BookFootballPitchDto> findAllTimeBookingMost() {
        List<BookFootballPitchDto> bookFootballPitchDtoList = new ArrayList<>();
        List<BookFootballPitchEntity> bookFootballPitchEntityList = bookFootballPitchRepository.findAllTimeBookingMost();
        for (BookFootballPitchEntity bookFootballPitchEntity : bookFootballPitchEntityList
        ) {
            BookFootballPitchDto bookFootballPitchDto =
                    beanConfig.modelMapper().map(bookFootballPitchEntity, BookFootballPitchDto.class);
            bookFootballPitchDto.setTimeBookingMost
                    (bookFootballPitchEntity.getFootballPitchScheduleEntityBooking().getTimeStart());
            bookFootballPitchDto.setNamePitchBookingMost(
                    bookFootballPitchEntity.getFootballPitchScheduleEntityBooking()
                            .getSubPitchEntitySchedule().getFootballPitchEntitySub().getName()
            );
            bookFootballPitchDto.setStreetNumberPitchBookingMost(
                    bookFootballPitchEntity.getFootballPitchScheduleEntityBooking()
                            .getSubPitchEntitySchedule().getFootballPitchEntitySub().getStreetNumber()
            );
            bookFootballPitchDto.setWardPitchBookingMost(
                    bookFootballPitchEntity.getFootballPitchScheduleEntityBooking()
                            .getSubPitchEntitySchedule().getFootballPitchEntitySub().getWardEntityPitch().getWardName()
            );
            bookFootballPitchDto.setDistrictPitchBookingMost(
                    bookFootballPitchEntity.getFootballPitchScheduleEntityBooking()
                            .getSubPitchEntitySchedule().getFootballPitchEntitySub().getWardEntityPitch().getDistrictEntity().getDistrictName()
            );
            bookFootballPitchDtoList.add(bookFootballPitchDto);
        }
        return bookFootballPitchDtoList;
    }

    @Override
    public List<ProductDto> findAllTop5ProductSell() {
        List<ProductDto> productDtoList = new ArrayList<>();
        List<ProductsEntity> productsEntityList = productRepository.findAllTop5ProductSell();
        for (ProductsEntity productsEntity : productsEntityList
        ) {
            ProductDto productDto = beanConfig.modelMapper().map(productsEntity, ProductDto.class);
            productDto.setTotalQuantityProductSell(
                    productRepository.sumQuantityProductSellByProductId(productsEntity.getId()));
            productDto.setTotalPriceProductSell(
                    productRepository.sumTotalPriceProductSellByProductId(productsEntity.getId()));
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    @Override
    public List<FootBallPitchDto> findAllTop5PitchBigBookingMost() {
        List<FootBallPitchDto> footBallPitchDtoList = new ArrayList<>();
        List<FootballPitchEntity> footballPitchEntityList = footballPitchRepository.findAllTop5PitchBigBookingMost();
        for (FootballPitchEntity footballPitchEntity : footballPitchEntityList
        ) {
            FootBallPitchDto footBallPitchDto = beanConfig.modelMapper().map(footballPitchEntity, FootBallPitchDto.class);
            footBallPitchDto.setTotalBookingPitchBig(footballPitchRepository.
                    findCountBookingByFootballPitchBig(footballPitchEntity.getId()));
            footBallPitchDto.setWardName(footballPitchEntity.getWardEntityPitch().getWardName());
            footBallPitchDto.setDistrictName(footballPitchEntity.getWardEntityPitch().getDistrictEntity().getDistrictName());
            footBallPitchDto.setManagerFullName(footballPitchEntity.getUserEntityPitch().getFullName());
            footBallPitchDtoList.add(footBallPitchDto);
        }
        return footBallPitchDtoList;
    }

    @Override
    public List<ServiceDto> findAllServiceByQuantitySellMost() {
        List<ServiceDto> serviceDtoList = new ArrayList<>();
        List<ServiceEntity> serviceEntityList = serviceRepository.findAllByQuantitySellMost();
        for (ServiceEntity entity:
                serviceEntityList){
            ServiceDto serviceDto = beanConfig.modelMapper().map(entity,ServiceDto.class);
            serviceDto.setTotalQuantityThongKe(serviceRepository.sumQuantitySell(entity.getId()));
            serviceDto.setTotalPriceSell(serviceRepository.sumPriceSell(entity.getId()));
            serviceDtoList.add(serviceDto);
        }
        return serviceDtoList;
    }

    @Override
    public List<ReportFootball> findAllMonth() {
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
        ReportFootball reportFootball=null;
        int[] monthDoubleMap=footballPitchScheduleRepository.findAllMonth();
        List<ReportFootball> reportFootballList=new ArrayList<>();
        for (int i=1;i<=arrMonths.length;i++){
            reportFootball=new ReportFootball();
            reportFootball.setMonth(i);
            if (footballPitchScheduleRepository.findAllTotalPriceByMonthCheck(i)==null){
            }else {
                reportFootball.setTotalPrice(footballPitchScheduleRepository.findAllTotalPriceByMonth(i));
            }
            reportFootballList.add(reportFootball);
        }
        return reportFootballList;
    }

    @Override
    public List<ReportProducts> findAllMonthOfProducts() {
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
        ReportProducts reportProducts=null;
        //int[] monthProducts= orderDetailRepository.findAllMonthOfProducts();
        List<ReportProducts> reportProductsList=new ArrayList<>();
        for (int i=1;i<=arrMonths.length;i++){
            reportProducts=new ReportProducts();
            reportProducts.setMonth(i);
            if (orderDetailRepository.findAllTotalPriceOfMonthProductsCheck(i)==null){
            }else {
                reportProducts.setTotalPrice(orderDetailRepository.findAllTotalPriceOfMonthProducts(i));
            }
            reportProductsList.add(reportProducts);
        }
        return reportProductsList;
    }

    @Override
    public List<ReportServices> findAllMonthOfServices() {
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
        ReportServices reportServices=null;
        //int[] monthServices= pitchScheduleServiceRepository.findAllMonthOfService();
        List<ReportServices> reportServicesList=new ArrayList<>();
        for (int i=1;i<=arrMonths.length;i++){
            reportServices=new ReportServices();
            reportServices.setMonth(i);
            if (pitchScheduleServiceRepository.totalPriceOfServiceByMonthDouble(i)==null){
            }else {
                reportServices.setTotalPrice(pitchScheduleServiceRepository.totalPriceOfServiceByMonth(i));
            }
            reportServicesList.add(reportServices);
        }
        return reportServicesList;
    }
}
