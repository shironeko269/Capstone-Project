package com.edu.fud.projectfootbalpitch.service;

import com.edu.fud.projectfootbalpitch.dto.*;
import com.edu.fud.projectfootbalpitch.entity.FootballPitchEntity;
import com.edu.fud.projectfootbalpitch.entity.ProductsEntity;

import java.time.Month;
import java.util.List;
import java.util.Map;

public interface StatisticsService {
    //admin
    double statisticsSystem();
    int totalProducts();
    int totalUsers();
    int totalFootballPitch();
    List<UserDto> findAllBookingLimit5ByUser();
    List<BookFootballPitchDto> findAllTimeBookingMost();
    List<ProductDto> findAllTop5ProductSell();
    List<FootBallPitchDto> findAllTop5PitchBigBookingMost();
    List<ServiceDto> findAllServiceByQuantitySellMost();
    List<ReportFootball> findAllMonth();
    List<ReportProducts> findAllMonthOfProducts();
    List<ReportServices> findAllMonthOfServices();
}
