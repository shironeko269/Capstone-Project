package com.edu.fud.projectfootbalpitch.service;
import com.edu.fud.projectfootbalpitch.dto.BookFootballPitchDto;

import java.util.List;

public interface BookFootballPitchService {
    List<BookFootballPitchDto> findAll(long userId);
    BookFootballPitchDto save(BookFootballPitchDto bookFootballPitchDto);
    BookFootballPitchDto findByIdViewBookingDetail(long bookingId);
    void deleteBooking(long id);
    void deleteAllByStatusCancel();
    //vi
    Long saveOfVi(BookFootballPitchDto dto);
    //huy
    List<BookFootballPitchDto> findAllByUserID(long userID);
}
