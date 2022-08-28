package com.edu.fud.projectfootbalpitch.controller.admin;

import com.edu.fud.projectfootbalpitch.dto.*;
import com.edu.fud.projectfootbalpitch.repository.CategoryProductRepository;
import com.edu.fud.projectfootbalpitch.service.*;
import com.edu.fud.projectfootbalpitch.service.impl.EmailService;
import com.edu.fud.projectfootbalpitch.util.Message;
import org.aspectj.weaver.ast.Or;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryProductService categoryProductService;

    @Autowired
    private DistrictService districtService;

    @Autowired
    private FootbalPitchService footbalPitchService;

    @Autowired
    private WardService wardService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private PitchTypeService pitchTypeService;

    @Autowired
    private SubPitchService subPitchService;

    @Autowired
    private StatusOrderService statusOrderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private IsMemberService isMemberService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    //Pitch
    @ModelAttribute("districts")
    public List<DistrictDto> getDistricts() {
        return districtService.findAll();
    }

    @ModelAttribute("wards")
    public List<WardDto> getWards() {
        return wardService.findAll();
    }

    @ModelAttribute("listCategoryProduct")
    public List<CategoryProductDto> getCategoryProduct() {
        return categoryProductService.findAll();
    }

    @ModelAttribute("listSupplierProduct")
    public List<SupplierDto> getSupplierProduct() {
        return supplierService.findAll();
    }

    @ModelAttribute("listPitchType")
    private List<PitchTypeDto> getPitchType() {
        return pitchTypeService.findAll();
    }

    @ModelAttribute("listPitchForSubPitch")
    public List<FootBallPitchDto> getListPitchForPitchOld() {
        return footbalPitchService.findAll();
    }

    @ModelAttribute("listStatusOrder")
    public List<StatusOrderDto> getListStatusOrder() {
        return statusOrderService.findAll();
    }

    @ModelAttribute("listIsMember")
    public List<IsMemberDto> getListIsMember() {
        return isMemberService.findAll();
    }

    @ModelAttribute
    public void addCommonData(Model model,Principal principal){
        String username=principal.getName();
        UserDto userDto=userService.findOneByUserName(username);
        model.addAttribute("User",userDto);
    }
    @RequestMapping("/index")
    public String home(Model model) {
        double statisticsMoneySystem = statisticsService.statisticsSystem();
        int totalUser = statisticsService.totalUsers();
        int totalFootballPitch = statisticsService.totalFootballPitch();
        int totalProducts = statisticsService.totalProducts();
        List<UserDto> listCustomerBookingLimit5 = statisticsService.findAllBookingLimit5ByUser();
        List<BookFootballPitchDto> listBookingTimeMost = statisticsService.findAllTimeBookingMost();
        List<ProductDto> listProductTop5SellMost = statisticsService.findAllTop5ProductSell();
        List<FootBallPitchDto> listFootballPitchMost = statisticsService.findAllTop5PitchBigBookingMost();
        List<ServiceDto> listTop5ServiceSellMost = statisticsService.findAllServiceByQuantitySellMost();
        List<ReportFootball> reportFootballList = statisticsService.findAllMonth();
        List<ReportProducts> reportProductsList = statisticsService.findAllMonthOfProducts();
        List<ReportServices> reportServicesList = statisticsService.findAllMonthOfServices();
        model.addAttribute("reportServicesList", reportServicesList);
        model.addAttribute("reportProductsList", reportProductsList);
        model.addAttribute("reportFootballList", reportFootballList);
        model.addAttribute("listTop5ServiceSellMost", listTop5ServiceSellMost);
        model.addAttribute("listFootballPitchMost", listFootballPitchMost);
        model.addAttribute("listProductTop5SellMost", listProductTop5SellMost);
        model.addAttribute("listBookingTimeMost", listBookingTimeMost);
        model.addAttribute("listCustomerBookingLimit5", listCustomerBookingLimit5);
        model.addAttribute("statisticsMoneySystem", statisticsMoneySystem);
        model.addAttribute("totalUser", totalUser);
        model.addAttribute("totalFootballPitch", totalFootballPitch);
        model.addAttribute("totalProducts", totalProducts);
        return "admin/index";
    }

    @RequestMapping("/add-new-pitch")
    public String openAddNewPitch(Model model) {
        FootBallPitchDto footBallPitchDto = new FootBallPitchDto();
        footBallPitchDto.setIsEdit(false);
        model.addAttribute("objFootballPitch", footBallPitchDto);
        model.addAttribute("managers", userService.findAllByRoleAndNoManagerPitch());
        return "admin/pages/tables/addNewPitch";
    }

    @RequestMapping("/update-pitch/{pitchId}")
    public String openUpdateNewPitch(@PathVariable("pitchId") long pitchId, Model model) {
        Optional<FootBallPitchDto> optional = footbalPitchService.findPitchById(pitchId);
        FootBallPitchDto footBallPitchDto = new FootBallPitchDto();
        if (optional.isPresent()) {
            footBallPitchDto = optional.get();
            footBallPitchDto.setIsEdit(true);
            if (footBallPitchDto.getUserId() == null) {
                model.addAttribute("managers", userService.findAllByRoleAndNoManagerPitch());
            } else {
                List<UserDto> allUserRoleManagerAndUserId = userService.findAllUserRoleManagerAndUserId(optional.get().getUserId());
                model.addAttribute("allUserRoleManagerAndUserId", allUserRoleManagerAndUserId);
            }
        }
        model.addAttribute("objFootballPitch", footBallPitchDto);
        return "admin/pages/tables/addNewPitch";
    }

    @PostMapping("/add-new-pitch")
    public String addNewFootballPitch(@Valid @ModelAttribute("objFootballPitch") FootBallPitchDto footBallPitchDto,
                                      BindingResult result, @RequestParam("imageParam") MultipartFile file, Model model,
                                      HttpSession session) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("objFootballPitch", footBallPitchDto);
                return "admin/pages/tables/addNewPitch";
            }
            if (file.isEmpty()) {
                footBallPitchDto.setImage("default.png");
            } else {
                //file the file to folder anh update the name to contact
                footBallPitchDto.setImage(file.getOriginalFilename());
                File saveFile = new ClassPathResource("static/images/image-pitch").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            if (footBallPitchDto.getId() != null) {
                Optional<FootBallPitchDto> opt = footbalPitchService.findPitchById(footBallPitchDto.getId());
                String streetNumber = footbalPitchService.findStreetNumberByPitchId(footBallPitchDto.getId());
                String urlMap = footbalPitchService.findUrlMapByPitchId(footBallPitchDto.getId());
                if (footBallPitchDto.getStreetNumber().equals(streetNumber) && footBallPitchDto.getUrlMap().equals(urlMap)) {
                    if (opt.isPresent()) {
                        FootBallPitchDto oldPitchDto = opt.get();
                        if (!file.isEmpty()) {
//                delete img old
//                            File deleteFile = new ClassPathResource("static/images/image-product").getFile();
//                            if (deleteFile.exists()) {
//                                File file1 = new File(deleteFile, oldPitchDto.getImage());
//                                file1.delete();
//                            }
//                update new image
                            footBallPitchDto.setImage(file.getOriginalFilename());
                            File saveFile = new ClassPathResource("static/images/image-pitch").getFile();
                            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                        } else {
                            footBallPitchDto.setImage(opt.get().getImage());
                        }
                        footbalPitchService.save(footBallPitchDto);
                        session.setAttribute("message", new Message("Sửa sân bóng thành công!"
                                , "alert-success"));
                        return "redirect:/admin/update-pitch/"+footBallPitchDto.getId();
                    }
                } else {
                    if (!footbalPitchService.findAllStreetNumber(footBallPitchDto.getStreetNumber()).isPresent()) {
                        if (!footbalPitchService.findAllUrlMap(footBallPitchDto.getUrlMap()).isPresent()) {
                            if (opt.isPresent()) {
                                FootBallPitchDto oldPitchDto = opt.get();
                                if (!file.isEmpty()) {
//                delete img old
                                    File deleteFile = new ClassPathResource("static/images/image-pitch").getFile();
                                    if (deleteFile.exists()) {
                                        File file1 = new File(deleteFile, oldPitchDto.getImage());
                                        file1.delete();
                                    }
//                update new image
                                    footBallPitchDto.setImage(file.getOriginalFilename());
                                    File saveFile = new ClassPathResource("static/images/image-pitch").getFile();
                                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                                    Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                                } else {
                                    footBallPitchDto.setImage(opt.get().getImage());
                                }
                                footbalPitchService.save(footBallPitchDto);
                                session.setAttribute("message", new Message("Sửa sân bóng thành công!"
                                        , "alert-success"));
                                return "redirect:/admin/update-pitch/"+footBallPitchDto.getId();
                            }
                        }else {
                            session.setAttribute("message", new Message("Địa chỉ google map sân bóng trong khu vực này đã trùng lặp!Nếu thay đổi tên đường vui lòng thay đổi cả địa chỉ map!"
                                    , "alert-danger"));
                            return "redirect:/admin/update-pitch/"+footBallPitchDto.getId();
                        }
                    } else {
                        session.setAttribute("message", new Message("Tên đường sân bóng trong khu vực này đã trùng lặp! Nếu thay đổi tên đường vui lòng thay đổi cả địa chỉ map!"
                                , "alert-danger"));
                        return "redirect:/admin/update-pitch/"+footBallPitchDto.getId();
                    }
                }
            } else {
                if (!footbalPitchService.findAllStreetNumber(footBallPitchDto.getStreetNumber()).isPresent()) {
                    if (!footbalPitchService.findAllUrlMap(footBallPitchDto.getUrlMap()).isPresent()) {
                        footbalPitchService.save(footBallPitchDto);
                        session.setAttribute("message", new Message("Đăng kí thành công!Bây giờ bạn hãy tạo các sân bóng trong khu vực này!"
                                , "alert-success"));
                    } else {
                        session.setAttribute("message", new Message("Địa chỉ google map sân bóng trong khu vực này đã trùng lặp!"
                                , "alert-danger"));
                        return "redirect:/admin/add-new-pitch";
                    }
                } else {
                    session.setAttribute("message", new Message("Tên đường sân bóng trong khu vực này đã trùng lặp!"
                            , "alert-danger"));
                    return "redirect:/admin/add-new-pitch";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Xảy ra lỗi!" + e.getMessage(),
                    "alert-danger"));
            return "redirect:/admin/add-new-pitch";
        }
        return "redirect:/admin/add-new-sub-pitch";
    }

    @DeleteMapping("/delete-pitch/{pitchId}")
    public String deletePitch(@PathVariable("pitchId") Long pitchId, HttpSession session) {
        try {
            this.footbalPitchService.deletePitch(pitchId);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        session.setAttribute("message", new Message("Xóa thành công sân có id là -> " + pitchId, "success"));
        return "redirect:/admin/list-pitch";
    }

    @RequestMapping("/add-new-sub-pitch")
    public String openAddNewSubPitch(Model model) {
        SubPitchDto subPitchDto = new SubPitchDto();
        model.addAttribute("objSubPitch", subPitchDto);
        return "admin/pages/tables/addNewSubPitch";
    }

    @PostMapping("/add-new-sub-pitch")
    public String addNewSubPitch(@Valid @ModelAttribute("objSubPitch") SubPitchDto subPitchDto,
                                 BindingResult result, @RequestParam("imageParam1") MultipartFile file1,
                                 @RequestParam("imageParam2") MultipartFile file2,
                                 @RequestParam("imageParam3") MultipartFile file3,
                                 @RequestParam("imageParam4") MultipartFile file4,
                                 @RequestParam("imageParam5") MultipartFile file5,
                                 Model model, HttpSession session) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("objSubPitch", subPitchDto);
                return "admin/pages/tables/addNewSubPitch";
            }

            if (file1.isEmpty()) {
                subPitchDto.setImage1("default.png");
            } else {
                //file the file to folder anh update the name to contact
                subPitchDto.setImage1(file1.getOriginalFilename());
                File saveFile = new ClassPathResource("static/images/image-subpitch").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file1.getOriginalFilename());
                Files.copy(file1.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            //image 2
            if (file2.isEmpty()) {
                subPitchDto.setImage2("default.png");
            } else {
                //file the file to folder anh update the name to contact
                subPitchDto.setImage2(file2.getOriginalFilename());
                File saveFile = new ClassPathResource("static/images/image-subpitch").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file2.getOriginalFilename());
                Files.copy(file2.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            //image 3
            if (file3.isEmpty()) {
                subPitchDto.setImage3("default.png");
            } else {
                //file the file to folder anh update the name to contact
                subPitchDto.setImage3(file3.getOriginalFilename());
                File saveFile = new ClassPathResource("static/images/image-subpitch").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file3.getOriginalFilename());
                Files.copy(file3.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            //image 4
            if (file4.isEmpty()) {
                subPitchDto.setImage4("default.png");
            } else {
                //file the file to folder anh update the name to contact
                subPitchDto.setImage4(file4.getOriginalFilename());
                File saveFile = new ClassPathResource("static/images/image-subpitch").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file4.getOriginalFilename());
                Files.copy(file4.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            //image 5
            if (file5.isEmpty()) {
                subPitchDto.setImage5("default.png");
            } else {
                //file the file to folder anh update the name to contact
                subPitchDto.setImage5(file5.getOriginalFilename());
                File saveFile = new ClassPathResource("static/images/image-subpitch").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file5.getOriginalFilename());
                Files.copy(file5.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            if (subPitchDto.getId() != null) {
                Optional<SubPitchDto> opt = subPitchService.findById(subPitchDto.getId());
                if (opt.isPresent()) {
                    SubPitchDto oldSubPitchDto = opt.get();
                    if (!file1.isEmpty()) {
//                delete img old
                        File deleteFile = new ClassPathResource("static/images/image-subpitch").getFile();
                        if (deleteFile.exists()) {
                            File image1 = new File(deleteFile, oldSubPitchDto.getImage1());
                            image1.delete();
                        }
//                update new image
                        subPitchDto.setImage1(file1.getOriginalFilename());
                        File saveFile = new ClassPathResource("static/images/image-subpitch").getFile();
                        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file1.getOriginalFilename());
                        Files.copy(file1.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        subPitchDto.setImage1(opt.get().getImage1());
                    }
                }
            }

            FootBallPitchDto footBallPitchDtoMaxId = footbalPitchService.findByIdMax();
            subPitchDto.setFootballPitchId(footBallPitchDtoMaxId.getId());
            subPitchService.save(subPitchDto);
            session.setAttribute("message", new Message("Hoàn thiện sân bóng thành công!"
                    , "alert-success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        return "admin/pages/tables/addNewSubPitch";
    }

    @RequestMapping("/profile-pitch")
    public String openProfile(Model model) {
        model.addAttribute("footballPitchMax", footbalPitchService.findByIdMax());
        List<SubPitchDto> subPitchDtoListByPitchMax = subPitchService.findAllByFootballPitchEntitySub();
        model.addAttribute("ListSubPitchDtoListByPitchMax", subPitchDtoListByPitchMax);
        model.addAttribute("managerMax", userService.findByUserMax());
        return "admin/pages/tables/profilePitch";
    }

    @RequestMapping("/add-new-sub-pitch-by-pitch")
    public String openAddNewSubPitchByPitch(Model model) {
        SubPitchDto subPitchDtoByPitch = new SubPitchDto();
        model.addAttribute("objSubPitchByPitch", subPitchDtoByPitch);
        return "admin/pages/tables/addNewSubPitchByPitch";
    }

    @RequestMapping("/update-sub-pitch-by-pitch/{subPitchId}")
    public String openUpdateSubPitchByPitch(@PathVariable("subPitchId") long subPitchId, Model model) {
        Optional<SubPitchDto> optional = subPitchService.findById(subPitchId);
        SubPitchDto subPitchDto = new SubPitchDto();
        if (optional.isPresent()) {
            subPitchDto = optional.get();
            subPitchDto.setIsEdit(true);
        }
        model.addAttribute("objSubPitchByPitch", subPitchDto);
        return "admin/pages/tables/addNewSubPitchByPitch";
    }

    @PostMapping("/add-new-sub-pitch-by-pitch")
    public String addNewSubPitchByPitchOld(@Valid @ModelAttribute("objSubPitchByPitch") SubPitchDto subPitchDto,
                                           BindingResult result, @RequestParam("imageParam1") MultipartFile file1,
                                           @RequestParam("imageParam2") MultipartFile file2,
                                           @RequestParam("imageParam3") MultipartFile file3,
                                           @RequestParam("imageParam4") MultipartFile file4,
                                           @RequestParam("imageParam5") MultipartFile file5,
                                           Model model, HttpSession session) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("objSubPitchByPitch", subPitchDto);
                return "admin/pages/tables/addNewSubPitchByPitch";
            }

            if (file1.isEmpty()) {
                subPitchDto.setImage1("default.png");
            } else {
                //file the file to folder anh update the name to contact
                subPitchDto.setImage1(file1.getOriginalFilename());
                File saveFile = new ClassPathResource("static/images/image-subpitch").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file1.getOriginalFilename());
                Files.copy(file1.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            //image 2
            if (file2.isEmpty()) {
                subPitchDto.setImage2("default.png");
            } else {
                //file the file to folder anh update the name to contact
                subPitchDto.setImage2(file2.getOriginalFilename());
                File saveFile = new ClassPathResource("static/images/image-subpitch").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file2.getOriginalFilename());
                Files.copy(file2.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            //image 3
            if (file3.isEmpty()) {
                subPitchDto.setImage3("default.png");
            } else {
                //file the file to folder anh update the name to contact
                subPitchDto.setImage3(file3.getOriginalFilename());
                File saveFile = new ClassPathResource("static/images/image-subpitch").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file3.getOriginalFilename());
                Files.copy(file3.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            //image 4
            if (file4.isEmpty()) {
                subPitchDto.setImage4("default.png");
            } else {
                //file the file to folder anh update the name to contact
                subPitchDto.setImage4(file4.getOriginalFilename());
                File saveFile = new ClassPathResource("static/images/image-subpitch").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file4.getOriginalFilename());
                Files.copy(file4.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            //image 5
            if (file5.isEmpty()) {
                subPitchDto.setImage5("default.png");
            } else {
                //file the file to folder anh update the name to contact
                subPitchDto.setImage5(file5.getOriginalFilename());
                File saveFile = new ClassPathResource("static/images/image-subpitch").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file5.getOriginalFilename());
                Files.copy(file5.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            if (subPitchDto.getId() != null) {
                Optional<SubPitchDto> opt = subPitchService.findById(subPitchDto.getId());
                if (opt.isPresent()) {
                    SubPitchDto oldSubPitchDto = opt.get();
                    //image 1
                    if (!file1.isEmpty()) {
//                delete img old
//                        File deleteFile = new ClassPathResource("static/images/image-subpitch").getFile();
//                        if (deleteFile.exists()) {
//                            File image1 = new File(deleteFile, oldSubPitchDto.getImage1());
//                            image1.delete();
//                        }
//                update new image
                        subPitchDto.setImage1(file1.getOriginalFilename());
                        File saveFile = new ClassPathResource("static/images/image-subpitch").getFile();
                        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file1.getOriginalFilename());
                        Files.copy(file1.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        subPitchDto.setImage1(opt.get().getImage1());
                    }
                    //image 2
                    if (!file2.isEmpty()) {
//                delete img old
//                        File deleteFile = new ClassPathResource("static/images/image-subpitch").getFile();
//                        if (deleteFile.exists()) {
//                            File image2 = new File(deleteFile, oldSubPitchDto.getImage2());
//                            image2.delete();
//                        }
//                update new image
                        subPitchDto.setImage2(file2.getOriginalFilename());
                        File saveFile = new ClassPathResource("static/images/image-subpitch").getFile();
                        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file2.getOriginalFilename());
                        Files.copy(file2.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        subPitchDto.setImage2(opt.get().getImage2());
                    }
                    //image 3
                    if (!file3.isEmpty()) {
//                delete img old
//                        File deleteFile = new ClassPathResource("static/images/image-subpitch").getFile();
//                        if (deleteFile.exists()) {
//                            File image3 = new File(deleteFile, oldSubPitchDto.getImage3());
//                            image3.delete();
//                        }
//                update new image
                        subPitchDto.setImage3(file3.getOriginalFilename());
                        File saveFile = new ClassPathResource("static/images/image-subpitch").getFile();
                        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file3.getOriginalFilename());
                        Files.copy(file3.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        subPitchDto.setImage3(opt.get().getImage3());
                    }
                    //image 4
                    if (!file4.isEmpty()) {
//                delete img old
//                        File deleteFile = new ClassPathResource("static/images/image-subpitch").getFile();
//                        if (deleteFile.exists()) {
//                            File image4 = new File(deleteFile, oldSubPitchDto.getImage4());
//                            image4.delete();
//                        }
//                update new image
                        subPitchDto.setImage4(file4.getOriginalFilename());
                        File saveFile = new ClassPathResource("static/images/image-subpitch").getFile();
                        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file4.getOriginalFilename());
                        Files.copy(file4.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        subPitchDto.setImage4(opt.get().getImage4());
                    }
                    if (!file5.isEmpty()) {
//                delete img old
//                        File deleteFile = new ClassPathResource("static/images/image-subpitch").getFile();
//                        if (deleteFile.exists()) {
//                            File image5 = new File(deleteFile, oldSubPitchDto.getImage5());
//                            image5.delete();
//                        }
//                update new image
                        subPitchDto.setImage5(file5.getOriginalFilename());
                        File saveFile = new ClassPathResource("static/images/image-subpitch").getFile();
                        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file5.getOriginalFilename());
                        Files.copy(file5.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        subPitchDto.setImage5(opt.get().getImage5());
                    }
                }
            }
            subPitchService.saveByPitchOld(subPitchDto);
            session.setAttribute("message", new Message("Lưu sân bóng nhỏ thành công!"
                    , "alert-success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        return "admin/pages/tables/addNewSubPitchByPitch";
    }

    @DeleteMapping("/delete-sub-pitch/{subPitchId}")
    public String deleteSubPitch(@PathVariable("subPitchId") Long subPitchId, HttpSession session) {
        //       Optional<ProductDto> opt =productService.findById(productId);
        try {
//            if (opt.isPresent()) {
//                File deleteFile=new ClassPathResource("static/images/image-product").getFile();
//                ProductDto productDto=opt.get();
//                if (deleteFile.exists()){
//                    File file1=new File(deleteFile,productDto.getImage());
//                    file1.delete();
//                }
            this.subPitchService.deleteSubPitch(subPitchId);
            //}
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        session.setAttribute("message", new Message("Xóa thành công sân có id là -> " + subPitchId, "success"));
        return "redirect:/admin/list-pitch";
    }

    @RequestMapping("/list-pitch")
    public String openListPitch(Model model) {
        List<FootBallPitchDto> footBallPitchDtoList = footbalPitchService.findAll();
        model.addAttribute("footBallPitchDtoListBig", footBallPitchDtoList);
        return "admin/pages/tables/listPitch";
    }

    @RequestMapping("/list-sub-pitch/{pitchId}")
    public String openListSubPitch(@PathVariable("pitchId") long pitchId, Model model) {
        List<SubPitchDto> subPitchDtoList = subPitchService.findAndByIdPitchBig(pitchId);
        model.addAttribute("subPitchDtoList", subPitchDtoList);
        return "admin/pages/tables/listSubPitch";
    }

    //Admin
    @RequestMapping("/account")
    public String openAccountPage(Model model) {
        model.addAttribute("objAddAdmin", new UserDto());
        return "admin/pages/accountAdmin/admin";
    }

    @PostMapping("/add-admin")
    public String addAccountAdmin(@Valid @ModelAttribute("objAddAdmin") UserDto userDto, BindingResult result,
                                  @RequestParam("imageAdmin") MultipartFile file, Model model, HttpSession session) {
        try {
            if (!userService.getUserByUsername(userDto.getUserName()).isPresent()
            ) {
                if (!userService.getUserByGmail(userDto.getGmail()).isPresent()) {
                    if (result.hasErrors()) {
                        model.addAttribute("objAddAdmin", userDto);
                        return "redirect:/admin/account";
                    }
                    if (file.isEmpty()) {
                        //if file empty then try our message
                        userDto.setImage("default.png");
                    } else {
                        //file the file to folder anh update the name to contact
                        userDto.setImage(file.getOriginalFilename());
                        File saveFile = new ClassPathResource("static/images/avt-admin").getFile();
                        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    }
                    userService.saveAdmin(userDto);
                    session.setAttribute("message", new Message("Đăng kí quản trị viên thành công!Bây giờ bạn có thể đăng nhập vào hệ thống!"
                            , "success"));
                } else {
                    session.setAttribute("message", new Message("Gmail đã tồn tại!",
                            "danger"));
                }
            } else {
                session.setAttribute("message", new Message("Tên đăng nhập đã tồn tại!",
                        "danger"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!" + e.getMessage(),
                    "danger"));
        }
        return "redirect:/admin/account";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("passwordOld") String oldPassword,
                                 @RequestParam("passwordNew") String newPassword,
                                 Principal principal, HttpSession session, HttpServletRequest httpServletRequest) {
        String username = principal.getName();
        Optional<UserDto> currentUser = this.userService.getUserByUsername(username);
        UserDto userDto = currentUser.get();
        if (this.bCryptPasswordEncoder.matches(oldPassword, userDto.getPassword())) {
            userDto.setPassword(newPassword);
            this.userService.saveAdmin(userDto);
            session.setAttribute("message", new Message("Bạn đã thay đổi mật khẩu thành công...", "success"));
        } else {
            session.setAttribute("message", new Message("Bạn đã nhập sai mật khẩu cũ...", "danger"));
            return "redirect:/admin/account";
        }
        return "redirect:/admin/account";
    }

    //Customer
    @RequestMapping("/list-customer")
    public String openListCustomer(Model model) {
        List<UserDto> listCustomer = userService.findAllUserMostOrder();
        UserDto userDto = new UserDto();
        model.addAttribute("objCustomer", userDto);
        model.addAttribute("listCustomer", listCustomer);
        return "admin/pages/customers/listCustomers";
    }

    @PostMapping("/update-customer")
    public String updateCustomer(@ModelAttribute("objCustomer") UserDto userDto, Model model,
                                 HttpSession session, HttpServletRequest request) {
        try {
            String customerId = request.getParameter("customerId");
            String customerUserName = request.getParameter("customerUserName");
            String customerFullName = request.getParameter("customerFullName");
            String customerGmail = request.getParameter("customerGmail");
            String customerPhone = request.getParameter("customerPhone");
            String customerAddress = request.getParameter("customerAddress");
            String customerImage = request.getParameter("customerImage");
            String customerPassword = request.getParameter("customerPassword");
            String customerIsMember = request.getParameter("customerIsMember");
            userDto.setId(Long.parseLong(customerId));
            userDto.setUserName(customerUserName);
            userDto.setPassword(customerPassword);
            userDto.setFullName(customerFullName);
            userDto.setGmail(customerGmail);
            userDto.setPhone(customerPhone);
            userDto.setAddress(customerAddress);
            userDto.setImage(customerImage);
            userDto.setIsMemberId(Long.parseLong(customerIsMember));
            userService.save(userDto);
            session.setAttribute("message", new Message("Thay đổi thành viên thành công!"
                    , "alert-success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        return "redirect:/admin/list-customer";
    }

    //Manger
    @RequestMapping("/add-manager")
    public String openAddManager(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("objEmployee", userDto);
        return "admin/pages/employee/addNewEmployee";
    }

    @PostMapping("/add-manager")
    public String addAccountEmployee(@Valid @ModelAttribute("objEmployee") UserDto userDto, BindingResult result,
                                     @RequestParam("imageEmployee") MultipartFile file, Model model, HttpSession session) {
        try {
            if (!userService.getUserByUsername(userDto.getUserName()).isPresent()
            ) {
                if (!userService.getUserByGmail(userDto.getGmail()).isPresent()) {
                    if (result.hasErrors()) {
                        model.addAttribute("objEmployee", userDto);
                        return "redirect:/admin/add-manager";
                    }
                    if (file.isEmpty()) {
                        //if file empty then try our message
                        userDto.setImage("default.png");
                    } else {
                        //file the file to folder anh update the name to contact
                        userDto.setImage(file.getOriginalFilename());
                        File saveFile = new ClassPathResource("static/images/avt-manager").getFile();
                        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    }
                    userService.saveManager(userDto);
                    session.setAttribute("message", new Message("Đăng kí nhân viên thành công!"
                            , "success"));
                } else {
                    session.setAttribute("message", new Message("Gmail đã tồn tại!",
                            "danger"));
                }
            } else {
                session.setAttribute("message", new Message("Tên đăng nhập đã tồn tại!",
                        "danger"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!" + e.getMessage(),
                    "danger"));
        }
        return "redirect:/admin/add-manager";
    }

    @RequestMapping("/list-manager")
    public String openListManager(Model model) {
        List<UserDto> listEmployee = userService.findAndUserByRoleManager();
        model.addAttribute("listEmployeeDto", listEmployee);
        return "admin/pages/employee/listEmployee";
    }

    @DeleteMapping("/delete-employee/{employeeId}")
    public String deleteEmployee(@PathVariable("employeeId") Long employeeId, HttpSession session) {
        try {
            this.userService.deleteUserManager(employeeId);
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        session.setAttribute("message", new Message("Xóa thành công nhân viên có id là -> " + employeeId, "success"));
        return "redirect:/admin/list-manager";
    }

    //Order
    @RequestMapping("/list-order")
    public String openListOrder(Model model) {
        List<OrderDto> orderDtoList = orderService.findALl();
        OrderDto orderDtoNew = new OrderDto();
//        List<OrderDto> listObjOrder=new ArrayList<>();
//        for (OrderDto orderDto:orderDtoList
//             ) {
//            orderDtoNew.setId(orderDto.getId());
//            orderDtoNew.setUserName(orderDto.getUserName());
//            orderDtoNew.setPhone(orderDto.getPhone());
//            orderDtoNew.setTotalPrice(orderDto.getTotalPrice());
//            orderDtoNew.setAddress(orderDto.getAddress());
//            orderDtoNew.setNote(orderDto.getNote());
//            orderDtoNew.setUserId(orderDto.getUserId());
//            orderDtoNew.setPaymentOrderId(orderDto.getPaymentOrderId());
//            orderDtoNew.setStatusId(orderDto.getStatusId());
//            listObjOrder.add(orderDto);
//        }
//        model.addAttribute("listObjOrder",listObjOrder);
        model.addAttribute("objOrder", orderDtoNew);
        model.addAttribute("listOrder", orderDtoList);
        return "admin/pages/listOrder/listOrder";
    }
    //tao
//    @RequestMapping("/edit-order/{orderId}")
//    public String openUpdateOrder(Model model, @PathVariable("orderId") Long orderId) {
//        OrderDto optional=orderService.findOne(orderId);
//        model.addAttribute("objOrder", optional);
//        return "admin/pages/listOrder/updateOrder";
//    }

    @PostMapping("/update-order")
    public String updateOrder(@ModelAttribute("objOrder") OrderDto orderDto, Model model, HttpSession session, HttpServletRequest request) {
        try {
            String itemOrderId = request.getParameter("itemOrderId");
            String itemOrderPhone = request.getParameter("itemOrderPhone");
            String itemOrderUserId = request.getParameter("itemOrderUserId");
            String itemOrderUserName = request.getParameter("itemOrderUserName");
            String itemOrderTotalPrice = request.getParameter("itemOrderTotalPrice");
            String itemOrderPaymentOrderId = request.getParameter("itemOrderPaymentOrderId");
            String itemOrderAddress = request.getParameter("itemOrderAddress");
            String itemOrderNote = request.getParameter("itemOrderNote");
            String itemOrderStatusId = request.getParameter("itemOrderStatusId");
            //get email and send
            String itemOrderUserNameCreate = request.getParameter("itemOrderUserNameCreate");
            String itemOrderEmailUser = request.getParameter("itemOrderEmailUser");
            orderDto.setId(Long.parseLong(itemOrderId));
            orderDto.setPhone(itemOrderPhone);
            orderDto.setUserId(Long.parseLong(itemOrderUserId));
            orderDto.setUserName(itemOrderUserName);
            orderDto.setTotalPrice(Double.parseDouble(itemOrderTotalPrice));
            orderDto.setPaymentOrderId(Long.parseLong(itemOrderPaymentOrderId));
            orderDto.setAddress(itemOrderAddress);
            orderDto.setNote(itemOrderNote);
            orderDto.setStatusId(Long.parseLong(itemOrderStatusId));
            if (orderDto.getStatusId() == 1) {
                String subject = "Xác nhận đơn hàng";
                String message = "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
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
                        "                            <br> Chào mừng đến với <b> sân bóng Yotsuba<br>\n" +
                        "                        </div>\n" +
                        "                        <div style=\"font-size: 18px;\">\n" +
                        "                            <br> Cám ơn " + itemOrderUserNameCreate + " đã mua hàng tại hệ thống của chúng tôi<br>\n" +
                        "                            <br> Vui lòng truy cập vào trang web vào phần lịch sử đặt hàng để xem lại chi tiết!! <br>\n" +
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
                boolean flag = emailService.sendEmail(subject, message, itemOrderEmailUser);
                if (!flag) {
                    session.setAttribute("message", new Message("Có lỗi xảy ra!"
                            , "alert-danger"));
                    return "redirect:/admin/list-order";
                }
            }
            orderService.save(orderDto);
            session.setAttribute("message", new Message("Duyệt đơn thành công!"
                    , "alert-success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        return "redirect:/admin/list-order";
    }

    @RequestMapping("/view-detail-order/{orderId}")
    public String openDetailOrder(@PathVariable("orderId") long orderId, Model model) {
        OrderDto orderDetail = orderService.findOne(orderId);
        List<OrderDetailDto> listOrderDetail = orderDetailService.findAllByOrderId(orderId);
        model.addAttribute("listOrderDetail", listOrderDetail);
        model.addAttribute("orderDetail", orderDetail);
        return "admin/pages/listOrder/viewDetailOrder";
    }

    @DeleteMapping("/delete-order/{orderId}")
    public String deleteOrder(@PathVariable("orderId") Long orderId, HttpSession session) {
        //       Optional<ProductDto> opt =productService.findById(productId);
        try {
//            if (opt.isPresent()) {
//                File deleteFile=new ClassPathResource("static/images/image-product").getFile();
//                ProductDto productDto=opt.get();
//                if (deleteFile.exists()){
//                    File file1=new File(deleteFile,productDto.getImage());
//                    file1.delete();
//                }
            this.orderService.deleteOrder(orderId);
            //}
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "danger"));
        }
        session.setAttribute("message", new Message("Xóa thành công Đơn hàng có id là -> " + orderId, "success"));
        return "redirect:/admin/list-order";
    }

    @DeleteMapping("/delete-status-cancel")
    public String deleteOrderByStatusCancel(HttpSession session) {
        try {
            this.statusOrderService.deleteAllByStatusAndIdCancel();
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "danger"));
        }
        session.setAttribute("message", new Message("Xóa thành công Đơn hàng có id là -> ", "success"));
        return "redirect:/admin/list-order";
    }

    //Product
    @RequestMapping("/add-new-category")
    public String openAddNewCategory(Model model) {
        model.addAttribute("objCategoryProduct", new CategoryProductDto());
        return "admin/pages/ProductManager/addNewCategory";
    }

    @RequestMapping("/update-category/{categoryId}")
    public String openUpdateCategory(@PathVariable("categoryId") long categoryId, Model model) {
        Optional<CategoryProductDto> optional = categoryProductService.findOne(categoryId);
        if (optional.isPresent()) {
            CategoryProductDto categoryProductDto = optional.get();
            model.addAttribute("objCategoryProduct", categoryProductDto);
        }
        return "admin/pages/ProductManager/addNewCategory";
    }

    @RequestMapping("/list-category")
    public String openListCategory() {
        return "admin/pages/ProductManager/listCategory";
    }

    @DeleteMapping("/delete-category/{categoryId}")
    public String deleteCategoryProduct(@PathVariable("categoryId") Long categoryId, HttpSession session) {
        try {
            this.categoryProductService.deleteCategoryById(categoryId);
            //}
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        session.setAttribute("message", new Message("Xóa thành công thể loại có id là -> " + categoryId, "success"));
        return "redirect:/admin/list-category";
    }


    @PostMapping("/add-new-category")
    public String addNewCategoryProduct(@Valid @ModelAttribute("objCategoryProduct")
                                                CategoryProductDto categoryProductDto, BindingResult result,
                                        Model model, HttpSession session) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("objCategoryProduct", categoryProductDto);
                return "admin/pages/ProductManager/addNewCategory";
            }
            if (categoryProductDto.getId() != null) {
                String name = categoryProductService.findNameByCategoryId(categoryProductDto.getId());
                if (name.equals(categoryProductDto.getName())) {
                    categoryProductService.save(categoryProductDto);
                    session.setAttribute("message", new Message("Thể loại chưa được sửa!"
                            , "alert-danger"));
                } else {
                    if (!categoryProductService.findAllByName(categoryProductDto.getName()).isPresent()) {
                        categoryProductService.save(categoryProductDto);
                        session.setAttribute("message", new Message("Sửa thể loại thành công!"
                                , "alert-success"));

                    } else {
                        session.setAttribute("message", new Message("Tên thể loại đã tồn tại!"
                                , "alert-danger"));
                    }
                }
            } else {
                if (!categoryProductService.findAllByName(categoryProductDto.getName()).isPresent()) {
                    categoryProductService.save(categoryProductDto);
                    session.setAttribute("message", new Message("Thêm mới thể loại thành công!"
                            , "alert-success"));
                } else {
                    session.setAttribute("message", new Message("Tên thể loại đã tồn tại!"
                            , "alert-danger"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        return "admin/pages/ProductManager/addNewCategory";
    }

    @RequestMapping("/add-new-product")
    public String openAddNewProduct(Model model) {
        ProductDto productDto = new ProductDto();
        //tao
        productDto.setIsEdit(false);
        model.addAttribute("objProduct", productDto);
        return "admin/pages/ProductManager/addNewProduct";
    }

    //tao
    @RequestMapping("/update-product/{productId}")
    public String openUpdateProduct(Model model, @PathVariable("productId") Long productId) {
        Optional<ProductDto> opt = productService.findById(productId);
        ProductDto productDto = new ProductDto();
        if (opt.isPresent()) {
            productDto = opt.get();
            //tao
            productDto.setIsEdit(true);
        }
        model.addAttribute("objProduct", productDto);
        return "admin/pages/ProductManager/addNewProduct";
    }

    //sua
    @PostMapping("/add-new-product")
    public String addNewProduct(@Valid @ModelAttribute("objProduct") ProductDto productDto,
                                BindingResult BindingResult, @RequestParam("imageProduct") MultipartFile file
            , Model model, HttpSession session) {
        try {
            if (BindingResult.hasErrors()) {
                model.addAttribute("objProduct", productDto);
                return "admin/pages/ProductManager/addNewProduct";
            }
            if (file.isEmpty()) {
                productDto.setImage("default.png");
            } else {
                //file the file to folder anh update the name to contact
                productDto.setImage(file.getOriginalFilename());
                File saveFile = new ClassPathResource("static/images/image-product").getFile();
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            if (productDto.getId() != null) {
                Optional<ProductDto> opt = productService.findById(productDto.getId());
                if (opt.isPresent()) {
                    ProductDto oldProductDto = opt.get();
                    if (!file.isEmpty()) {
//                delete img old
//                        File deleteFile = new ClassPathResource("static/images/image-product").getFile();
//                        if (deleteFile.exists()) {
//                            File file1 = new File(deleteFile, oldProductDto.getImage());
//                            file1.delete();
//                        }
//                update new image
                        productDto.setImage(file.getOriginalFilename());
                        File saveFile = new ClassPathResource("static/images/image-product").getFile();
                        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        productDto.setImage(opt.get().getImage());
                    }
                }
            }
            productService.save(productDto);
            session.setAttribute("message", new Message("Lưu sản phẩm thành công!"
                    , "alert-success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        return "admin/pages/ProductManager/addNewProduct";
    }

    @RequestMapping("/add-new-supplier")
    public String openAddNewSupplier(Model model) {
        model.addAttribute("objSupplier", new SupplierDto());
        return "admin/pages/ProductManager/addNewSupplier";
    }

    @PostMapping("/add-new-supplier")
    public String addNewSupplier(@Valid @ModelAttribute("objSupplier") SupplierDto supplierDto, BindingResult result,
                                 Model model, HttpSession session) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("objSupplier", supplierDto);
                return "admin/pages/ProductManager/addNewSupplier";
            }
            System.out.println("Footbal :" + supplierDto);
            supplierService.save(supplierDto);
            session.setAttribute("message", new Message("Thêm mới nhà cung cấp thành công!"
                    , "alert-success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Xảy ra lỗi!" + e.getMessage(),
                    "alert-danger"));
        }
        return "admin/pages/ProductManager/addNewSupplier";
    }

    @RequestMapping("/list-product")
    public String openListProduct(Model model) {
        model.addAttribute("listProduct", productService.findAll());
        return "admin/pages/ProductManager/listProduct";
    }

    //tao
    @DeleteMapping("/delete-product/{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId, HttpSession session) {
        //       Optional<ProductDto> opt =productService.findById(productId);
        try {
//            if (opt.isPresent()) {
//                File deleteFile=new ClassPathResource("static/images/image-product").getFile();
//                ProductDto productDto=opt.get();
//                if (deleteFile.exists()){
//                    File file1=new File(deleteFile,productDto.getImage());
//                    file1.delete();
//                }
            this.productService.deleteProduct(productId);
            //}
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        session.setAttribute("message", new Message("Xóa thành công sản phẩm có id là -> " + productId, "success"));
        return "redirect:/admin/list-product";
    }
}
