package com.edu.fud.projectfootbalpitch.controller;

import com.edu.fud.projectfootbalpitch.config.GooglePojo;
import com.edu.fud.projectfootbalpitch.config.GoogleUtils;
import com.edu.fud.projectfootbalpitch.dto.*;
import com.edu.fud.projectfootbalpitch.entity.DistrictEntity;
import com.edu.fud.projectfootbalpitch.entity.FootballPitchEntity;
import com.edu.fud.projectfootbalpitch.entity.ProductsEntity;
import com.edu.fud.projectfootbalpitch.repository.UserRepository;
import com.edu.fud.projectfootbalpitch.service.*;
import com.edu.fud.projectfootbalpitch.service.impl.EmailService;
import com.edu.fud.projectfootbalpitch.util.Message;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.Errors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private GoogleUtils googleUtils;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private WardService wardService;
    @Autowired
    private SubPitchService subPitchService;
    @Autowired
    private PitchTypeService pitchTypeService;
    @Autowired
    private FootbalPitchService footballPitchService;
    @Autowired
    private CategoryProductService categoryProductService;
    @Autowired
    private Environment env;

    @ModelAttribute("categories")
    public List<CategoryProductDto> getCategories(){
        return categoryProductService.findAll();
    }

    @ModelAttribute("listDistrict")
    List<DistrictDto> allDistrict() {
        return districtService.findAll();
    }

    @ModelAttribute("listWard")
    List<WardDto> allWard() {
        return wardService.findAll();
    }

    @ModelAttribute("listPitch")
    List<FootBallPitchDto> allPitch() {
        return footballPitchService.findAllCuaVi();
    }

    @ModelAttribute("listProduct")
    List<ProductDto> allProduct(){return productService.findAll();}

    @RequestMapping("")
    public String openIndex(Model model) {
        return "redirect:/index";
    }
    @RequestMapping("/index")
    public String openHome(Model model) {

        List<ProductDto> productsEntityList=productService.findLimitByDate();
        model.addAttribute("products",productsEntityList);

        List<FootBallPitchDto> footballPitchEntityList=footballPitchService.findLimitByDate();
        model.addAttribute("footballPitch",footballPitchEntityList);

        return "index";
    }
    @RequestMapping("/about")
    public String openBbout() {
        return "about";
    }

    @RequestMapping("/sign-in")
    public String openLogin() {
        return "login";
    }
    @RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
    public ModelAndView accessDenied() {
        return new ModelAndView("redirect:/sign-in?accessDenied");
    }
    @RequestMapping("/login-google")
    public String loginGoogle(HttpServletRequest request) throws ClientProtocolException, IOException {
        String code = request.getParameter("code");
        String type = env.getProperty("google.link.get.scope");
        if (code == null || code.isEmpty()) {
            return "redirect:/login?google=error";
        }
        String accessToken = googleUtils.getToken(code, type);

        GooglePojo googlePojo = googleUtils.getUserInfo(accessToken);
        System.out.println(googlePojo.toString());
        UserDetails userDetail = googleUtils.buildUser(googlePojo);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
                userDetail.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/user/profile";
    }

    @RequestMapping("/access-denied")
    public String openError() {
        return "error";
    }

    @RequestMapping("/register")
    public String openRegister(Model model) {
        model.addAttribute("objUser", new UserDto());
        return "register";
    }

    @PostMapping("/send-otp")
    public String sendOTP(@RequestParam("email") String email, HttpSession session) {
        System.out.println("Email: " + email);
        Random random = new Random(System.currentTimeMillis());
        int otp = (int) Math.floor(((Math.random() * 89999999) + 10000000));
        System.out.println("OTP: " + otp);
        String subject = "OTP From SCM";
        String message = ""
                + "<div style='border:1px solid #e2e2e2;padding:20px'>"
                + "<h1>"
                + "Mã OTP là :"
                + "<b>" + otp
                + "</n>"
                + "</h1>"
                + "</div>";
        String to = email;
        boolean flag = emailService.sendEmail(subject, message, to);
        if (flag) {
            session.setAttribute("myotp", otp);
            session.setAttribute("email", email);
            return "register";
        } else {
            session.setAttribute("message", "Kiểm tra lại email !!");
            return "register";
        }
    }

    @RequestMapping(value = "/add-register", method = RequestMethod.POST)
    public String addUser(@ModelAttribute("objUser") @Valid UserDto userDto, BindingResult result,
                          @RequestParam("profileImage") MultipartFile file,@RequestParam("otp") Integer otp
            , Model model
            , HttpSession session) {
        try {
            if (!userService.getUserByUsername(userDto.getUserName()).isPresent()) {
                if (!userService.getUserByGmail(userDto.getGmail()).isPresent()) {
                    if (result.hasErrors()) {
                        System.out.println("ERROR" + result.toString());
                        model.addAttribute("objUser", userDto);
                        return ("register");
                    }
                    int myOtp = (int) session.getAttribute("myotp");
                    if (myOtp==otp){
                        if (file.isEmpty()) {
                            //if file empty then try our message
                            userDto.setImage("default.png");
                        } else {
                            //file the file to folder anh update the name to contact
                            userDto.setImage(file.getOriginalFilename());
                            File saveFile = new ClassPathResource("static/images/avt-user").getFile();
                            Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                        }
                        System.out.println("User :" + userDto);
                        userService.save(userDto);
                        session.setAttribute("message", new Message("Đăng kí thành công! Bây giờ bạn có thể đăng nhập vào hệ thống!"
                                , "alert-success"));
//                        return "redirect:/sign-in";
                    }else {
                    session.setAttribute("messageErrorOtp", new Message("OTP sai!",
                            "alert-danger"));
                     }
                } else {
                    session.setAttribute("message", new Message("Gmail đã tồn tại!",
                            "alert-danger"));
                }
            } else {
                session.setAttribute("message", new Message("Tên đăng nhập đã tồn tại!",
                        "alert-danger"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("messageErrorOtp", new Message("Có lỗi xảy ra",
                    "alert-danger"));
        }
        return ("register");
    }

    //product
    @RequestMapping("/products")
    public String openProductmode(Model model,
                                  @Param("keyword") String keyword)
    {
        List<ProductDto> productsEntityList=productService.findAllPagined(keyword);
        model.addAttribute("productsall",productsEntityList);

        return "products";
    }
    @GetMapping("/products/getCategory/{categoryId}")
    public String openProductByCate(Model model,
                                    @PathVariable("categoryId")long categoryId){

        List<ProductDto> productDtoList =productService.findAllByCate(categoryId);
        model.addAttribute("productsall",productDtoList);

        return "products";
    }

    @RequestMapping("/product-detail/value={productId}/category={categoryId}")
    public String openProductDetail(@PathVariable("productId")long productId, Model model,
                                    @PathVariable("categoryId")long categoryId) {
        List<ProductDto> productDtoList =productService.findOneByID(productId);
        model.addAttribute("detailProduct1",productDtoList);

        List<ProductDto> productDtoList1 =productService.findAllByCate(categoryId);
        model.addAttribute("detailProduct2",productDtoList1);

        return "detailProduct";
    }

    //booking home
    @RequestMapping("/booking")
    public String openBookingPitch() {
        return "Booking";
    }

    @RequestMapping("/booking-pitch-detail")
    public String openBookingPitchDetail() {
        return "detailPitch";
    }

    @RequestMapping("/booking-sub-pitch-detail")
    public String openBookingSubPitchDetail() {
        return "bookingSubPitch";
    }
    @RequestMapping("/booking/pitch/{pitchId}")
    public String openPitchDetail(@PathVariable(value = "pitchId") long id, Model model) {
        FootBallPitchDto pitch = footballPitchService.findPitchByPitchId(id);
        UserDto managerInfo = userService.findUserById(pitch.getUserId());

        List<SubPitchDto> subPitchDtoList = subPitchService.findAllSubPitchByPitchId(id);

        List<PitchTypeDto> pitchTypeDtoList = pitchTypeService.findPitchTypeByListId(subPitchDtoList);
        model.addAttribute("listPitchType", pitchTypeDtoList);
        model.addAttribute("pitch", pitch);
        model.addAttribute("listSubPitch", subPitchDtoList);
        model.addAttribute("managerInfo", managerInfo);
        model.addAttribute("firstSubPitch", subPitchDtoList.get(0));
        return "detailPitch";
    }


    @RequestMapping("/booking/search")
    public String searchPitchByName(Model model, HttpServletRequest request) {
        String name = request.getParameter("keyword");
        List<FootBallPitchDto> footBallPitchDtoList = footballPitchService.findAllByName(name);
        model.addAttribute("keyword", name);
        model.addAttribute("listPitch", footBallPitchDtoList);
        return "Booking";
    }
    // forgot password
    @RequestMapping("/forgot")
    public String openEmailForm() {
        Random random = new Random(System.currentTimeMillis());
        int otp = (int) Math.floor(((Math.random() * 8999) + 1000));
        System.out.println("OTp-:--------" + otp);
        return "forgot_email_form";
    }

    @PostMapping("/send-otp-forgot")
    public String sendOTPForgot(@RequestParam("email_forgot") String email, HttpSession session) {
        System.out.println("Email: " + email);
        Random random = new Random(System.currentTimeMillis());
        int otp = (int) Math.floor(((Math.random() * 8999) + 1000));
        System.out.println("OTP: " + otp);
        String subject = "OTP From SCM";
        String message = ""
                + "<div style='border:1px solid #e2e2e2;padding:20px'>"
                + "<h1>"
                + "Mã OTP là :"
                + "<b>" + otp
                + "</n>"
                + "</h1>"
                + "</div>";
        String to = email;
        Optional<UserDto> user = this.userService.getUserByGmail(email);
        if (user.isPresent()) {
            boolean flag = this.emailService.sendEmail(subject, message, to);
            if (flag) {
                session.setAttribute("myotp", otp);
                session.setAttribute("email", email);
                return "verify_otp";
            } else {
                session.setAttribute("message", "Kiểm tra lại mail !!");
                return "forgot_email_form";
            }
        } else {
            session.setAttribute("message", "Kiểm tra lại mail !!");
            return "forgot_email_form";
        }
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp_forgot") Integer otp, HttpSession session) {
        int myOtp = (int) session.getAttribute("myotp");
        String email = (String) session.getAttribute("email");
        if (myOtp == otp) {
            Optional<UserDto> user = this.userService.getUserByGmail(email);
            if (!user.isPresent()) {
                session.setAttribute("message", "Email này không tồn tại trong hệ thống!!");
                return "forgot_email_form";
            } else {

            }
            return "password_change_form";
        } else {
            session.setAttribute("message", "Bạn nhập sai mã OTP!");
            return "verify_otp";
        }
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("newpassword") String newpassword, HttpSession session) {
        String email = (String) session.getAttribute("email");
        UserDto user = this.userService.getUserByGmail(email).get();
        user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
        this.userService.save(user);
        return "redirect:/sign-in?change=password_changed_successfully!";
    }
}
