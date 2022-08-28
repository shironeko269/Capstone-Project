package com.edu.fud.projectfootbalpitch.response;

import com.edu.fud.projectfootbalpitch.dto.FootballPitchScheduleDto;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Main {
    public Time covertStringToTime(String timeStr) throws ParseException {
        Time t = null;
        if (!timeStr.equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            long ms = sdf.parse(timeStr).getTime();
            t = new Time(ms);
        }
        return t;
    }

    public String checkTimeUser(Time st, Time end) {
        String check;
        if (st == null) {
            check = "bạn chưa chọn giờ bắt đầu";
            return check;
        }if (end == null) {
            check = null;
            return check;
        }
        if (st == null && end == null) {
            check = "vui lòng chọn khung giờ đặt sân";
            return check;
        } else {
            long sub = (end.getTime() - st.getTime()) / 60000;
            if (st.after(end)) {
                check = "giờ bắt đầu bạn chọn không hợp lệ";
                return check;
            } else {
                if (sub < 60) {
                    check = "vui lòng đặt sân ít nhất 60 phút";
                    return check;
                } else {
                    check = "khung giờ bạn chọn hợp lệ";
                    return check;
                }
            }
        }
    }

    public String checkTimeBooked(Time startInput, Time endInput, Time start, Time end) {
        String check = "";
        if (startInput == null) {
            startInput = endInput;
        }
        if (endInput == null) {
            endInput = startInput;
        }
        if (startInput == null && endInput == null) {
            check = "giờ bắt đầu và giờ kết thúc bạn chọn không được trống";
            return check;
        }
        if ((start != null && end != null)) {
            if (startInput.after(start) && startInput.before(end) || startInput.after(endInput)) {
                check = "giờ bắt đầu bạn chọn không hợp lệ";
                return check;
            }
            if (endInput.after(start) && endInput.before(end)) {
                check = "giờ kết thúc bạn chọn không hợp lệ";
                return check;
            }
            if ((startInput.before(start) || startInput.equals(start)) && (endInput.after(end) || endInput.equals(end))) {
                check = "khung giờ bạn chọn không hợp lệ";
                return check;
            } else {
                long sub = (endInput.getTime() - startInput.getTime()) / 60000;
                System.out.println(sub);
                if (sub < 60) {
                    check = "vui lòng đặt sân ít nhất 60 phút";
                    return check;
                } else {
                    check = "khung giờ bạn chọn hợp lệ";
                }
            }
        } else {
            check = "giờ bắt đầu, kết thúc rỗng";
        }
        return check;
    }

    public String checkTimeByList(Time start, Time end, List<FootballPitchScheduleDto> List) {
        String check = "";
        if (start == null) {
            check = "vui lòng chọn giờ bắt đầu";
            return check;
        }if (end == null) {
            check = null;
            return check;
        }
        if (List.size() == 0) {
            check = checkTimeUser(start, end);
            return check;
        } else {
            for (FootballPitchScheduleDto dto : List) {
                System.out.println("Giờ đã được đặt :" + dto.getTimeStart() + " - " + dto.getTimeEnd());
                System.out.println("Giờ bạn chọn :" + start + " - " + end);
                check = checkTimeBooked(start, end, dto.getTimeStart(), dto.getTimeEnd());
                if (check!="khung giờ bạn chọn hợp lệ"){
                    return check;
                }else {
                    continue;
                }
            }
        }
        return check;
    }


    public static void main(String[] args) throws ParseException {
        Main main = new Main();
        Time stChoose = main.covertStringToTime("6:30");
        Time enChoose = main.covertStringToTime("9:00");
        Time st = main.covertStringToTime("5:00");
        Time en = main.covertStringToTime("5:28");
//        System.err.println(main.checkTimeBooked(stChoose, enChoose, st, en));
        System.err.println(main.checkTimeUser(st, en));

    }
}
