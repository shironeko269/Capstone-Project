package com.edu.fud.projectfootbalpitch.controller.user;

import com.edu.fud.projectfootbalpitch.dto.*;
import com.edu.fud.projectfootbalpitch.repository.ProductRepository;
import com.edu.fud.projectfootbalpitch.response.Main;
import com.edu.fud.projectfootbalpitch.service.*;
import com.edu.fud.projectfootbalpitch.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.ParseException;
import java.util.*;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private FootbalPitchService footballPitchService;
    @Autowired
    private SubPitchService subPitchService;
    @Autowired
    private PaymentBookingService paymentBookingService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private FootballPitchScheduleService footballPitchScheduleService;
    @Autowired
    private PitchScheduleServiceService pitchScheduleServiceService;
    @Autowired
    private BookFootballPitchService bookFootballPitchService;
    @Autowired
    private UserService userService;
    @Autowired
    private ServiceService service;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PaymentOrderService paymentOrderService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    Main main = new Main();

    //profile
    @RequestMapping("/profile")
    public String openProfile(Principal principal, Model model) {
        String username = principal.getName();
        UserDto userDto = userService.findOneByUserName(username);
        model.addAttribute("profile", userDto);
        return "user/profile";
    }

    @RequestMapping("/edit-profile/{userId}")
    public String openUpdateProduct(Model model, @PathVariable("userId") Long userId) {
        Optional<UserDto> opt = userService.findById(userId);
        UserDto userDto = new UserDto();
        if (opt.isPresent()) {
            userDto = opt.get();
            //tao
            userDto.setIsEdit(true);
        }
        model.addAttribute("userProfile", userDto);
        return "user/updateProfile";
    }

    @PostMapping("/add-edit-profile")
    public String addNewProduct(@Valid @ModelAttribute("userProfile") UserDto userDto,
                                BindingResult BindingResult, @RequestParam("profileImage") MultipartFile file
            , Model model, HttpSession session) {
        try {
            if (BindingResult.hasErrors()) {
                model.addAttribute("userProfile", userDto);
                return "user/updateProfile";
            }
//            if (file.isEmpty()) {
//                userDto.setImage("default.png");
//            } else {
//                //file the file to folder anh update the name to contact
//                userDto.setImage(file.getOriginalFilename());
//                File saveFile = new ClassPathResource("static/images/avt-user").getFile();
//                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
//                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//            }
            if (userDto.getId() != null) {
                Optional<UserDto> opt = userService.findById(userDto.getId());
                if (opt.isPresent()) {
                    UserDto oldUserDto = opt.get();
                    if (!file.isEmpty()) {
//                delete img old
                        File deleteFile = new ClassPathResource("static/images/avt-user").getFile();
                        if (deleteFile.exists()) {
                            File file1 = new File(deleteFile, oldUserDto.getImage());
                            file1.delete();
                        }
//                update new image
                        userDto.setImage(file.getOriginalFilename());
                        File saveFile = new ClassPathResource("static/images/avt-user").getFile();
                        Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());
                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        userDto.setImage(opt.get().getImage());
                    }
                }
            }
            userService.saveUpdateProfile(userDto);
            session.setAttribute("message", new Message("Cập nhật thành công!"
                    , "alert-success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        return "user/updateProfile";
    }

    @RequestMapping("/change-password")
    public String openChangePassword(Principal principal, Model model) {
        String username = principal.getName();
        UserDto userDto = userService.findOneByUserName(username);
        model.addAttribute("profile", userDto);
        return "user/changePass";
    }

    @PostMapping("/add-change-password")
    public String addChangePassword(@RequestParam("oldPassword") String oldPassword,
                                    @RequestParam("newPassword") String newPassword,
                                    @RequestParam("confirmPassword") String confirmPassword,
                                    Principal principal, HttpSession session) {
        try {
            String username = principal.getName();
            UserDto userDto = userService.findOneByUserName(username);
            System.out.println("============" + userDto.getPassword() + "=============");

            if (this.passwordEncoder.matches(oldPassword, userDto.getPassword())) {
                if (newPassword.equals(confirmPassword)) {
                    userDto.setPassword(this.passwordEncoder.encode(newPassword));
                    this.userService.savePass(userDto);
                    session.setAttribute("message", new Message("Cập nhật mật khẩu thành công", "success"));
                    return "redirect:/user/change-password";

                } else {
                    session.setAttribute("message", new Message("Xác nhận mật khẩu không khớp ...", "danger"));
                    return "redirect:/user/change-password";
                }
            } else {
                session.setAttribute("message", new Message("Vui lòng nhập mật khẩu cũ chính xác !!", "danger"));
                return "redirect:/user/change-password";
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        return "user/changePass";
    }

    //cart
    @RequestMapping("/cart")
    public String openCart(Principal principal, Model model, HttpSession session) {
        String username = principal.getName();
        System.out.println(username);

        UserDto userDto = userService.findAllByUsername(username);

        long member = userDto.getIsMemberId();

        System.out.println(member);

        model.addAttribute("member", member);

        return "user/cart";
    }

    //invoice
    @RequestMapping("/history-invoice/{userId}")
    public String openHistoryInvoice(Model model, @PathVariable("userId") Long userId, Principal principal) {
        List<OrderDto> orderDtoList = orderService.findAllByUserID(userId);
        System.out.println("===============" + orderDtoList + "=================");
        model.addAttribute("historyInvoice", orderDtoList);
        String username = principal.getName();
        UserDto userDto = userService.findOneByUserName(username);
        model.addAttribute("profile", userDto);
        return "user/historyInvoice";

    }

    @DeleteMapping("/delete-invoice/{orderID}")
    public String deleteProduct(@PathVariable("orderID") Long orderID, HttpSession session, Principal principal) {
        //       Optional<ProductDto> opt =productService.findById(productId);
        String username = principal.getName();
        UserDto optional = userService.findOneByUserName(username);
        try {
            if (optional.getId() != null) {
                this.orderService.deleteOrderID(orderID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Có lỗi xảy ra!"
                    , "alert-danger"));
        }
        session.setAttribute("message", new Message("Xóa thành công sản phẩm có id là -> " + orderID, "success"));
        return "redirect:/user/history-invoice/" + optional.getId();
    }

    @RequestMapping("/history-invoice-detail/{orderID}")
    public String openHistoryInvoiceDetail(@PathVariable("orderID") long orderID, Model model, Principal principal) {
        String username = principal.getName();
        UserDto userDto = userService.findOneByUserName(username);
        List<OrderDetailDto> orderDetailDtoList = orderDetailService.findAllByOrderID(orderID, userDto.getId());
        model.addAttribute("historyInvoiceDetail", orderDetailDtoList);
        model.addAttribute("userID", userDto);
        return "user/historyInvoiceDetail";
    }

    @RequestMapping("/history-invoice-booking")
    public String openHistoryInvoiceBooking(Principal principal, Model model) {
        String username = principal.getName();
        UserDto userDto = userService.findOneByUserName(username);
        model.addAttribute("profile", userDto);
        List<BookFootballPitchDto> bookFootballPitchDtoList = bookFootballPitchService.findAllByUserID(userDto.getId());
        model.addAttribute("historyInvoiceBooking", bookFootballPitchDtoList);
        return "user/historyInvoiceBooking";
    }

    @RequestMapping("/history-invoice-booking-service/{schedulePitchID}")
    public String openHistoryInvoiceBookingService(@PathVariable("schedulePitchID") Long schedulePitchID, Model model, Principal principal) {
        String username = principal.getName();
        UserDto userDto = userService.findOneByUserName(username);
        List<PitchScheduleServiceDto> pitchScheduleServiceDtoList = pitchScheduleServiceService.findAllByScheduleServiceEntities(schedulePitchID, userDto.getId());
        model.addAttribute("service", pitchScheduleServiceDtoList);
        model.addAttribute("userID", userDto);
        return "user/historyInvoiceDetailService";
    }

    //checkout
    @RequestMapping("/history-invoice-booking-pitch/{schedulePitchID}")
    public String openHistoryInvoiceBookingPitch(@PathVariable("schedulePitchID") Long schedulePitchID, Model model, Principal principal) {
        String username = principal.getName();
        UserDto userDto = userService.findOneByUserName(username);
        List<SubPitchDto> subPitchDtoList = subPitchService.findAllBySubPitch(schedulePitchID, userDto.getId());
        model.addAttribute("pitch", subPitchDtoList);
        model.addAttribute("userID", userDto);
        return "user/historyInvoiceDetailPitch";
    }

    //checkout
    @RequestMapping("/checkout")
    public String openCheckOut(Principal principal, Model model) {

        String username = principal.getName();

        UserDto userDto = userService.findAllByUsername(username);

        long member = userDto.getIsMemberId();

        model.addAttribute("member", member);
        model.addAttribute("userin4", userDto);

//        List<PaymentOrderDto> paymentOrderDtoList =paymentOrdertService.findAllPayment();
//        model.addAttribute("paymentlist",paymentOrderDtoList);

        OrderDto orderDto = new OrderDto();

        orderDto.setUserName(userDto.getFullName());
        orderDto.setAddress(userDto.getAddress());
        orderDto.setUserId(userDto.getId());
        orderDto.setPhone(userDto.getPhone());
//        orderDto.setPaymentOrderId(this.paymentOrdertService.findByOrderPaymentPaypalId(data.get("order_id").toString()).getId());
//        System.out.println("id payment: "+orderDto.getPaymentOrderId());
        OrderDetailDto orderDetailDto = null;


        model.addAttribute("objOrder", orderDto);

        return "user/ProceedToCheckout";
    }

    @Autowired
    private ProductRepository productRepository;

    @PostMapping(value = "/checkout/add-order")
    public synchronized String addUser(@ModelAttribute("objOrder") @Valid OrderDto orderDto,
                                       BindingResult result,
                                       Model model, HttpSession session, Principal principal
    ) {
        String username = principal.getName();
        try {
            if (result.hasErrors()) {
                System.out.println("ERROR" + result.toString());
                model.addAttribute("objOrder", orderDto);
                return ("user/ProceedToCheckout");
            }
            PaymentOrderDto paymentOrderDto = paymentOrderService.findByOrderPaymentPaypalId(orderDto.getOrderPaymentId());
            System.out.println("id payment: " + paymentOrderDto.getId());
            long idorder = orderService.saveOfDuc(orderDto, username, paymentOrderDto.getId()).getId();
            OrderDetailDto orderDetailDto = new OrderDetailDto();
            for (int i = 0; i < orderDto.getProductId().length; i++) {
                if (productRepository.checkQuantity(orderDto.getProductId()[i]) != null) {
                    orderDetailDto.setProductId(orderDto.getProductId()[i]);
                    orderDetailDto.setQuantity(orderDto.getQuantity()[i]);
                    orderDetailService.save(orderDetailDto, idorder);
                    productService.updateQuantity(orderDetailDto.getQuantity(), orderDetailDto.getProductId());
                } else {
                    session.setAttribute("message", new Message("Hết hàng !"
                            , "alert-success"));
                }
            }
            OrderDto dto = orderService.findOne(idorder);
            idorder = orderService.saveOfDuc(dto, username, paymentOrderDto.getId()).getId();
            model.addAttribute("idorder", idorder);
//            session.setAttribute("message", new Message("Đặt hàng thành công !"
//                    , "alert-success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Something Went Wrong!" + e.getMessage(),
                    "alert-danger"));
        }
        return ("/user/ProceedToCheckout");
    }

    //duc
    @PostMapping("/create_order_product")
    @ResponseBody
    public String createOrderProduct(@RequestBody Map<String, Object> data) throws RazorpayException {
        System.out.println(data);
        int amt = Integer.parseInt(data.get("amount").toString());
        RazorpayClient client = new RazorpayClient("rzp_test_SYzQTN6MOUcF9L", "76s7mN0tA2Ybope1HnEYfinv");
        JSONObject object = new JSONObject();
        object.put("amount", amt);
        object.put("currency", "INR");
        object.put("receipt", "txn_235425");
        Order order = client.Orders.create(object);
        System.out.println(order);
        PaymentOrderDto myOrder = new PaymentOrderDto();
        myOrder.setAmount(order.get("amount") + "");
        myOrder.setOrderPaymentPaypalId(order.get("id"));
        myOrder.setPayment_paypal_id(null);
        myOrder.setStatus("Chưa thanh toán");
        myOrder.setReceipt(order.get("receipt"));
        this.paymentOrderService.save(myOrder);
//        System.out.println("id payment: "+(data.get("order_id").toString()));
        //if you want you can save this to your data....
        return order.toString();
    }

    @PostMapping("/update_order_product")
    public ResponseEntity<?> updateOrderProduct(@RequestBody Map<String, Object> data,Principal principal) {
        PaymentOrderDto myOrder = this.paymentOrderService.findByOrderPaymentPaypalId(data.get("order_id").toString());
        myOrder.setPayment_paypal_id(data.get("payment_id").toString());
        myOrder.setStatus(data.get("status").toString());
        long idpayment = this.paymentOrderService.save(myOrder);
//        System.out.println("id order: "+ orderDto.getId());
//        System.out.println("id payment: "+(this.paymentOrdertService.findByOrderPaymentPaypalId(data.get("order_id").toString()).getId()) );
        System.out.println(data);
        Map<String, String> temp = new HashMap<String, String>();
        temp.put("msg", "updated");
        temp.put("userId",userService.getUserByUsername(principal.getName()).get().getId().toString());
        return ResponseEntity.ok(Collections.unmodifiableMap(temp));
    }

    @RequestMapping("/booking/sub-pitch/{subPitchId}")
    public String openBookingSubPitchDetail(@PathVariable(value = "subPitchId") long id, Model model, Principal principal) {
        Optional<UserDto> optional = userService.getUserByUsername(principal.getName());
        String name = optional.get().getFullName();
        model.addAttribute("name", name);
        String phone = optional.get().getPhone();
        model.addAttribute("phone", phone);
        SubPitchDto subPitchDto = subPitchService.findSubPitchBySubPitchId(id);
        model.addAttribute("subPitch", subPitchDto);
        long pitchId = subPitchDto.getFootballPitchId();
        model.addAttribute("pitchAddress", footballPitchService.findPitchByPitchId(pitchId).getAddress());
        List<ServiceDto> serviceDtoList1 = serviceService.findAllByCategoryId(1);
        model.addAttribute("serviceList1", serviceDtoList1);
        List<ServiceDto> serviceDtoList2 = serviceService.findAllByCategoryId(2);
        model.addAttribute("serviceList2", serviceDtoList2);
        return "bookingSubPitch";
    }

    @PostMapping("/create_order")
    @ResponseBody
    public String createOrder(@RequestBody Map<String, Object> data, Principal principal) throws RazorpayException {
        System.out.println(data);
        int amt = Integer.parseInt(data.get("amount").toString());
        RazorpayClient client = new RazorpayClient("rzp_test_SYzQTN6MOUcF9L", "76s7mN0tA2Ybope1HnEYfinv");
        JSONObject object = new JSONObject();
        object.put("amount", amt * 100);
        object.put("currency", "INR");
        object.put("receipt", "txn_235425");
        Order order = client.Orders.create(object);
        System.out.println(order);
        PaymentBookingDto myOrder = new PaymentBookingDto();
        myOrder.setAmount(String.valueOf(amt));
        myOrder.setOrderPaymentPaypalId(order.get("id"));
        myOrder.setPayment_paypal_id(null);
        myOrder.setStatus("Chưa thanh toán");
        myOrder.setReceipt(order.get("receipt"));
        this.paymentBookingService.save(myOrder);
        return order.toString();
    }

    @PostMapping("/update_order")
    public ResponseEntity<?> updateOrder(@RequestBody Map<String, Object> data, Model model) throws ParseException {
        PaymentBookingDto myOrder = this.paymentBookingService.findByOrderPaymentPaypalId(data.get("order_id").toString());
        myOrder.setPayment_paypal_id(data.get("payment_id").toString());
        myOrder.setStatus(data.get("status").toString());
        Long id = this.paymentBookingService.save(myOrder);
        System.out.println(id);
        System.out.println(data);
        Map<String, Long> temp = new HashMap<String, Long>();
        temp.put("idPayment", id);
        return ResponseEntity.ok(Collections.unmodifiableMap(temp));
    }

    @PostMapping("/save")
    public ResponseEntity<Object> saveBookingInfo(@RequestBody BookingInfo info, Principal principal) throws ParseException {

        for (ServiceDto serviceDto : info.getListService()) {
            System.out.println(serviceDto.getId());
        }
        FootballPitchScheduleDto footballPitchScheduleDto = new FootballPitchScheduleDto();
        footballPitchScheduleDto.setDateBooking(info.getDateBooking());
        footballPitchScheduleDto.setPrice(info.getHourFee());
        footballPitchScheduleDto.setTimeStart(main.covertStringToTime(info.getTimeStart()));
        footballPitchScheduleDto.setTimeEnd(main.covertStringToTime(info.getTimeEnd()));
        footballPitchScheduleDto.setSubPitchId(info.getSubPitchId());
        footballPitchScheduleDto.setStatus(1);
        long footballPitchScheduleId = footballPitchScheduleService.save(footballPitchScheduleDto);

        PitchScheduleServiceDto pitchScheduleServiceDto = new PitchScheduleServiceDto();
        for (ServiceDto serviceDto : info.getListService()) {
            pitchScheduleServiceDto.setPrice(serviceDto.getPrice());
            pitchScheduleServiceDto.setQuantity(serviceDto.getQuantity());
            service.updateQuantity(serviceDto.getQuantity(), serviceDto.getId());
            pitchScheduleServiceDto.setFootballPitchScheduleId(footballPitchScheduleId);
            pitchScheduleServiceDto.setServicePitchId(serviceDto.getId());
            pitchScheduleServiceService.save(pitchScheduleServiceDto);
        }
        BookFootballPitchDto bookFootballPitchDto = new BookFootballPitchDto();
        bookFootballPitchDto.setPreOrderPayment(info.getPreOrder());
        bookFootballPitchDto.setFootballPitchScheduleId(footballPitchScheduleId);
        bookFootballPitchDto.setPaymentBookingId(info.getPaymentId());
        bookFootballPitchDto.setNote(info.getNote());
        bookFootballPitchDto.setStatusBookFootballPitchId(2L);
        Optional<UserDto> optional = userService.getUserByUsername(principal.getName());
        bookFootballPitchDto.setUserId(optional.get().getId());
        System.out.println(bookFootballPitchService.saveOfVi(bookFootballPitchDto));

        return null;
    }
}
