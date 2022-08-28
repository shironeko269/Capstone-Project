package com.edu.fud.projectfootbalpitch.controller.manager;

import com.edu.fud.projectfootbalpitch.dto.*;
import com.edu.fud.projectfootbalpitch.service.*;
import com.edu.fud.projectfootbalpitch.service.impl.EmailService;
import com.edu.fud.projectfootbalpitch.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private CategoryServiceService categoryServiceService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private UserService userService;

    @Autowired
    private FootbalPitchService footbalPitchService;

    @Autowired
    private SubPitchService subPitchService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private StatusBookFootballPitchService statusBookFootballPitchService;

    @Autowired
    private BookFootballPitchService bookFootballPitchService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PitchScheduleServiceService pitchScheduleServiceService;

    @Autowired
    private Thongke thongke;

    @Autowired
    private FootballPitchScheduleService footballPitchScheduleService;

    @ModelAttribute("listCategoryService")
    public List<CategoryServiceDto> getCategoryService(){return categoryServiceService.findAll();}

    @ModelAttribute("listStatusBookFootballPitchService")
    public List<StatusBookFootballPitchDto> getStatusBookFootballPitch(){return statusBookFootballPitchService.findAll();}

    @ModelAttribute
    public void addCommonData(Model model,Principal principal){
        String username=principal.getName();
        UserDto userDto=userService.findOneByUserName(username);
            model.addAttribute("User",userDto);
    }
    // home
    @RequestMapping("/index")
    public String home(Model model, Principal principal,HttpServletRequest request){
        String username=principal.getName();
        Optional<UserDto> optionalUserDto=userService.getUserByUsername(username);
        FootBallPitchDto footBallPitchDto=footbalPitchService.findFootballPitchDtoByUserId(optionalUserDto.get().getId());
        List<SubPitchDto> subPitchDtoList= subPitchService.findAllSubPitchByUserId(optionalUserDto.get().getId());
        List<BookFootballPitchDto> listBookingTimeStartMostByManagerId=thongke.findAllTimeStartByManager(optionalUserDto.get().getId());
        List<ServiceDto> listServiceSellMost = thongke.findAllServiceByQuantitySellMostByManager(optionalUserDto.get().getId());
        List<UserDto> listUserReturnMostByManagerId = thongke.findAllUserByReturnByManager(optionalUserDto.get().getId());
        List<FootballPitchScheduleDto> footballPitchScheduleDtoList =
                footballPitchScheduleService.findAllNow(optionalUserDto.get().getId());
        double totalMoneyByManager = thongke.TotalMoneyByManager(optionalUserDto.get().getId());
        int totalBookingByMonthByManager = thongke.TotalBookingByMonthByManagerId(optionalUserDto.get().getId());
        int totalSubPitchByManager = thongke.TotalSubPitchByManagerId(optionalUserDto.get().getId());
        List<ReportPitch> totalPricePitchOfMonthByManagerId = thongke.findAllTotalPricePitchOfMonthByManagerId(optionalUserDto.get().getId());
        List<ReportService> totalPriceServiceOfMonthByManagerId = thongke.findAllTotalPriceServiceOfMonthByManagerId(optionalUserDto.get().getId());
        if (footBallPitchDto.getUserId() == optionalUserDto.get().getId()){
            model.addAttribute("listBookingTimeStartMostByManagerId",listBookingTimeStartMostByManagerId);
            model.addAttribute("managerNow",optionalUserDto.get());

            model.addAttribute("footballPitchDto",footBallPitchDto);
            model.addAttribute("listSubPitchDto",subPitchDtoList);
            model.addAttribute("listServiceSellMost",listServiceSellMost);
            model.addAttribute("listUserReturnMostByManagerId",listUserReturnMostByManagerId);
            model.addAttribute("totalMoneyByManager",totalMoneyByManager);
            model.addAttribute("totalBookingByMonthByManager",totalBookingByMonthByManager);
            model.addAttribute("totalSubPitchByManager",totalSubPitchByManager);
            model.addAttribute("totalPricePitchOfMonthByManagerId",totalPricePitchOfMonthByManagerId);
            model.addAttribute("totalPriceServiceOfMonthByManagerId",totalPriceServiceOfMonthByManagerId);
            model.addAttribute("footballPitchScheduleDtoList",footballPitchScheduleDtoList);
        }
        return "manager/index";
    }

    //update booking service
    @RequestMapping("/update-booking-service/{scheduleId}")
    public String openUpdateBookingService(@PathVariable("scheduleId")Long scheduleId,Model model){
        List<PitchScheduleServiceDto> pitchScheduleServiceDtoList =
                pitchScheduleServiceService.findAllByScheduleId(scheduleId);
//        PitchScheduleServiceDto pitchScheduleServiceDto = new PitchScheduleServiceDto();
        double sumOldScheduleService = pitchScheduleServiceService.sumOldScheduleService(scheduleId);
        model.addAttribute("sumOldScheduleService",sumOldScheduleService);
        model.addAttribute("scheduleId", scheduleId);
        model.addAttribute("objPitchScheduleService",pitchScheduleServiceDtoList);
        List<ServiceDto> serviceDtoList1 = serviceService.findAllByCategoryId(1);
        model.addAttribute("serviceList1", serviceDtoList1);
        List<ServiceDto> serviceDtoList2 = serviceService.findAllByCategoryId(2);
        model.addAttribute("serviceList2", serviceDtoList2);
        return "manager/pages/booking/UpdateBooking";
    }

    //change password
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("passwordOld") String oldPassword,
                                 @RequestParam("passwordNew") String newPassword,
                                 Principal principal, HttpSession session, HttpServletRequest httpServletRequest){
        String username=principal.getName();
        Optional<UserDto> currentUser = this.userService.getUserByUsername(username);
        UserDto userDto=currentUser.get();
        if (this.bCryptPasswordEncoder.matches(oldPassword,userDto.getPassword())){
            userDto.setPassword(newPassword);
            this.userService.saveManager(userDto);
            session.setAttribute("message",new Message("Bạn đã thay đổi mật khẩu thành công...","success"));
        }else {
            session.setAttribute("message",new Message("Bạn đã nhập sai mật khẩu cũ...","danger"));
            return "redirect:/manager/index";
        }
        return "redirect:/manager/index";
    }

    // add new service
    @RequestMapping("/add-booking")
    public String openNewBooking(Model model, Principal principal){
        String username=principal.getName();
        Optional<UserDto> optionalUserDto=userService.getUserByUsername(username);
        FootBallPitchDto footBallPitchDto=footbalPitchService.findFootballPitchDtoByUserId(optionalUserDto.get().getId());
        List<SubPitchDto> subPitchDtoList= subPitchService.findAllSubPitchByUserId(optionalUserDto.get().getId());
        if (footBallPitchDto.getUserId() == optionalUserDto.get().getId()) {
            model.addAttribute("managerNow", optionalUserDto.get());
            model.addAttribute("footballPitchDto", footBallPitchDto);
            model.addAttribute("listSubPitchDto", subPitchDtoList);
        }
        return "manager/pages/booking/addBooking";
    }

    //list booking
    @RequestMapping("/list-booking")
    public String listBooking(Model model,Principal principal){
        String username=principal.getName();
        Optional<UserDto> optionalUserDto=userService.getUserByUsername(username);
        BookFootballPitchDto bookFootballPitchDto=new BookFootballPitchDto();
        model.addAttribute("objBookingPitch",bookFootballPitchDto);
        model.addAttribute("listBookFootballPitchService",bookFootballPitchService.findAll(optionalUserDto.get().getId()));
        return "manager/pages/booking/listBooking";}

    @PostMapping("/update-booking")
    public String updateBooking(@ModelAttribute("objBookingPitch")BookFootballPitchDto bookFootballPitchDto,Model model,HttpSession session,HttpServletRequest request) {
        try{
            String itemBookingId=request.getParameter("itemBookingId");
            String itemBookingPreOrder=request.getParameter("itemBookingPreOrder");
            String itemBookingUserId=request.getParameter("itemBookingUserId");
            String itemBookingPaymentBookingId=request.getParameter("itemBookingPaymentBookingId");
            String itemBookingScheduleId=request.getParameter("itemBookingScheduleId");
            String itemBookingSubPitchName=request.getParameter("itemBookingSubPitchName");
            String itemBookingEmail=request.getParameter("itemBookingEmail");
            String itemBookingStatusId=request.getParameter("itemBookingStatusId");
            //get email and send
            bookFootballPitchDto.setId(Long.parseLong(itemBookingId));
            bookFootballPitchDto.setPreOrderPayment(Double.parseDouble(itemBookingPreOrder));
            bookFootballPitchDto.setUserId(Long.parseLong(itemBookingUserId));
            bookFootballPitchDto.setPaymentBookingId(Long.parseLong(itemBookingPaymentBookingId));
            bookFootballPitchDto.setFootballPitchScheduleId(Long.parseLong(itemBookingScheduleId));
            bookFootballPitchDto.setStatusBookFootballPitchId(Long.parseLong(itemBookingStatusId));
            if (bookFootballPitchDto.getStatusBookFootballPitchId()==1){
                String subject="Xác nhận đơn đặt sân";
                String message="<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                        "<head>\n" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                        "    <title>Java Techie Mail</title>\n" +
                        "</head>\n" +
                        "\n" +
                        "<body>\n" +
                        "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                        "    <tr>\n" +
                        "        <td align=\"center\" valign=\"top\" bgcolor=\"#838383\"\n" +
                        "            style=\"background-color: #838383;\"><br> <br>\n" +
                        "            <table width=\"600\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
                        "                <tr>\n" +
                        "                    <td align=\"center\" valign=\"top\" bgcolor=\"#d3be6c\"\n" +
                        "                        style=\"background-color: #d3be6c; font-family: Arial, Helvetica, sans-serif; font-size: 13px; color: #000000; padding: 0px 15px 10px 15px;\">\n" +
                        "\n" +
                        "                        <div style=\"font-size: 48px; color:blue;\">\n" +
                        "                            <b>Yotsuba</b>\n" +
                        "                        </div>\n" +
                        "\n" +
                        "                        <div style=\"font-size: 24px; color: #555100;\">\n" +
                        "                            <br> Chào mừng đến với <b> trang quản lý sân bóng Yotsuba<br>\n" +
                        "                        </div>\n" +
                        "                        <div style=\"font-size: 18px;\">\n" +
                        "                            <br> Cám ơn "+itemBookingEmail+" đã đặt sân tại hệ thống của chúng tôi<br>\n" +
                        "                            <br> Vui lòng truy cập vào trang web vào phần lịch sử đặt sân để xem lại chi tiết!! <br>\n" +
                        "                            <br>\"Bạn phải tới trước 15 phút để xác nhận nếu không sẽ hủy đơn theo quy định của hệ thống\" <br> <br>\n" +
                        "                            <br>\"Cám ơn quý khách đã ủng hộ\" <br> <br>\n" +
                        "                            <br> <br> <b>Mọi thắc mắc xin vui lòng liên hệ với chúng tôi theo 0123456789 hoặc tại website www.Yotsuba.com.vn</b><br><br>\n" +
                        "                            <br>\n" +
                        "                        </div>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "            </table> <br> <br></td>\n" +
                        "    </tr>\n" +
                        "</table>\n" +
                        "</body>\n" +
                        "</html>";
                boolean flag=emailService.sendEmail(subject,message,itemBookingEmail);
                if (!flag) {
                    session.setAttribute("message", new Message("Có lỗi xảy ra!"
                            , "alert-danger"));
                    return "redirect:/manager/list-booking";
                }
            }
            bookFootballPitchService.save(bookFootballPitchDto);
            session.setAttribute("message", new Message("Duyệt đơn thành công!"
                    , "alert-success"));
        }catch (Exception e){
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        return "redirect:/manager/list-booking";
    }
    @DeleteMapping("/delete-status-cancel")
    public String deleteAllByStatusCancel(HttpSession session){
        try {
            this.bookFootballPitchService.deleteAllByStatusCancel();
        }catch (Exception e){
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        session.setAttribute("message",new Message("Xóa thành công tất cả đơn đặt sân đã bị hủy","success"));
        return "redirect:/manager/list-booking";
    }

    //view booking detail
    @RequestMapping("/view-booking-detail/{bookingId}")
    public String ViewBookingDetail(@PathVariable("bookingId")long bookingId,Model model){
        BookFootballPitchDto bookFootballPitchDetail= bookFootballPitchService.findByIdViewBookingDetail(bookingId);
        List<PitchScheduleServiceDto> listServiceBooking= pitchScheduleServiceService.findAllServiceByPitchScheduleByBooking(bookingId);
        model.addAttribute("bookingPitchDetail",bookFootballPitchDetail);
        model.addAttribute("listServiceBooking",listServiceBooking);
        return "manager/pages/booking/viewDetailBooking";
    }

    //delete booking
    @DeleteMapping("/delete-booking/{bookFootballPitchId}")
    public String deleteBooking(@PathVariable("bookFootballPitchId")Long bookFootballPitchId,HttpSession session){
        try {
            this.bookFootballPitchService.deleteBooking(bookFootballPitchId);
        }catch (Exception e){
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        session.setAttribute("message",new Message("Xóa thành công đơn đặt sân có id là -> "+bookFootballPitchId,"success"));
        return "redirect:/manager/list-booking";
    }

    // add new category
    @RequestMapping("/add-category")
    public String openNewCategory(Model model){
        model.addAttribute("objCategoryService",new CategoryServiceDto());
        return "manager/pages/CategoryManager/addNewCategory";
    }

    //update category
    @RequestMapping("/update-category/{categoryId}")
    public String openUpdateCategory(Model model, @PathVariable("categoryId")Long categoryId){
        Optional<CategoryServiceDto> opt = categoryServiceService.findById(categoryId);
        CategoryServiceDto categoryServiceDto = new CategoryServiceDto();
        if (opt.isPresent()){
            categoryServiceDto = opt.get();
            categoryServiceDto.setIsEdit(true);
        }
        model.addAttribute("objCategoryService",categoryServiceDto);
        return "manager/pages/CategoryManager/addNewCategory";
    }

    // add or update category
    @PostMapping("/add-category")
    public String addNewCategory(@Valid @ModelAttribute("objCategoryService")
                                         CategoryServiceDto categoryServiceDto, BindingResult result,
                                 Model model, HttpSession session){
        try {
            if (result.hasErrors()){
                model.addAttribute("objCategoryService", categoryServiceDto);
                return "manager/pages/CategoryManager/addNewCategory";
            }
            List<CategoryServiceDto> findAllByCategoryName = categoryServiceService.findAllByCategoryName(categoryServiceDto.getName());
            //update
            if (categoryServiceDto.getId() != null) {
                Optional<CategoryServiceDto> opt = categoryServiceService.findById(categoryServiceDto.getId());
                if (findAllByCategoryName.isEmpty()) {
                    categoryServiceService.save(categoryServiceDto);
                    session.setAttribute("message", new Message("Đã sửa thể loại thành công!"
                            , "alert-success"));
                    return "manager/pages/CategoryManager/addNewCategory";
                } else if(opt.get().getName().equals(categoryServiceDto.getName())){
                    session.setAttribute("message", new Message("Thông tin danh mục chưa được sửa!"
                            , "alert-danger"));
                } else{
                    session.setAttribute("message", new Message("Tên danh mục đã tồn tại trong hệ thống!"
                            , "alert-danger"));
                }
            }else if (findAllByCategoryName.isEmpty()) {
                categoryServiceService.save(categoryServiceDto);
                session.setAttribute("message", new Message("Đã thêm mới thể loại thành công!"
                        , "alert-success"));
            }else{
                session.setAttribute("message", new Message("Danh mục dịch vụ này đã tồn tại trong hệ thống!"
                        , "alert-danger"));
            }
        }catch (Exception e){
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!",
                    "alert-danger"));
        }
        return "manager/pages/CategoryManager/addNewCategory";
    }

    //list category
    @RequestMapping("/list-category")
    public String listCategory(Model model){
        model.addAttribute("listCategoryService",categoryServiceService.findAll());
        return "manager/pages/CategoryManager/listCategory";
    }



    // add new service
    @RequestMapping("/add-service")
    public String openNewService(Model model){
        ServiceDto serviceDto = new ServiceDto();
        serviceDto.setIsEdit(false);
        model.addAttribute("objService", serviceDto);
        return "manager/pages/ServiceManager/addNewService";
    }
    // update service
    @RequestMapping("/update-service/{serviceId}")
    public String openUpdateService(Model model, @PathVariable("serviceId")Long serviceId){
        Optional<ServiceDto> opt = serviceService.findById(serviceId);
        ServiceDto serviceDto = new ServiceDto();
        if (opt.isPresent()){
            serviceDto = opt.get();
            serviceDto.setIsEdit(true);
        }
        model.addAttribute("objService",serviceDto);
        return "manager/pages/ServiceManager/addNewService";
    }

    @PostMapping("/add-service")
    public String addNewService(@Valid @ModelAttribute("objService")
                                        ServiceDto serviceDto, BindingResult result,
                                @RequestParam("imageService") MultipartFile file,
                                Model model, HttpSession session){
        try {
            if (result.hasErrors()){
                model.addAttribute("objService",serviceDto);
                return "manager/pages/ServiceManager/addNewService";
            } else {

//            if (file.isEmpty()){
//                serviceDto.setImage("default.png");
//            }else{
//                serviceDto.setImage(file.getOriginalFilename());
//                File saveFile = new ClassPathResource("static/images/image-service").getFile();
//                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
//                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
//            }
                if (serviceDto.getId() != null){
                    Optional<ServiceDto> opt = serviceService.findById(serviceDto.getId());
                    if (opt.isPresent()){
                        ServiceDto oldServiceDto = opt.get();
                        if (!file.isEmpty()){
    //                        File deleteFile = new ClassPathResource("static/images/image-service").getFile();
    //                        if (deleteFile.exists()){
    //                            File file1=new File(deleteFile,oldServiceDto.getImage());
    //                            file1.delete();
    //                        }
                            serviceDto.setImage(file.getOriginalFilename());
                            File saveFile = new ClassPathResource("static/images/image-service").getFile();
                            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                            Files.copy(file.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
                        }else{
                            serviceDto.setImage(opt.get().getImage());
                        }
                        serviceService.save(serviceDto);
                        session.setAttribute("message", new Message("Đã sửa dịch vụ thành công!"
                                , "alert-success"));
                        return "redirect:/manager/update-service/"+serviceDto.getId();
                    }

                } else {
                    if (file.isEmpty()){
                        serviceDto.setImage("default.png");
                    }else{
                        serviceDto.setImage(file.getOriginalFilename());
                        File saveFile = new ClassPathResource("static/images/image-service").getFile();
                        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                        Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                    }
                    serviceService.save(serviceDto);
                    session.setAttribute("message", new Message("Đã thêm mới dịch vụ thành công!"
                            , "alert-success"));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!",
                    "alert-danger"));
            return "redirect:/manager/add-service";
        }
        return "redirect:/manager/add-service";
    }


    // list service
    @RequestMapping("/list-service")
    public String listService(Model model){
        model.addAttribute("listService",serviceService.findAll());
        return "manager/pages/ServiceManager/listService";
    }


    // delete service
    @DeleteMapping("/delete-service/{serviceId}")
    public String deleteService(@PathVariable("serviceId")Long serviceId,HttpSession session){
        try {
            this.serviceService.deleteService(serviceId);
        }catch (Exception e){
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        session.setAttribute("message",
                new Message("Xóa thành công sản phẩm có id là -> "+serviceId,"success"));
        return "redirect:/manager/list-service";
    }

    // delete service
    @DeleteMapping("/delete-category/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId")Long categoryId,HttpSession session){
        try {
            this.categoryServiceService.deleteCategory(categoryId);
        }catch (Exception e){
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        session.setAttribute("message",
                new Message("Xóa thành công danh mục có id là -> "+categoryId,"success"));
        return "redirect:/manager/list-category";
    }
}