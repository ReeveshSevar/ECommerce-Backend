package com.sevar.ecommerce.controller;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sevar.ecommerce.model.CartItem;
import com.sevar.ecommerce.model.Orders;
import com.sevar.ecommerce.model.Users;
import com.sevar.ecommerce.repository.CartItemRepository;
import com.sevar.ecommerce.repository.OrderRepository;
import com.sevar.ecommerce.response.ApiResponse;
import com.sevar.ecommerce.response.PaymentLinkResponse;
import com.sevar.ecommerce.service.CartItemService;
import com.sevar.ecommerce.service.OrderService;
import com.sevar.ecommerce.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Value("${razorpay.api.key}")
    String apikey;

    @Value("${razorpay.api.secret}")
    String apiSecret;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/payment/{orderId}")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(@PathVariable Long orderId,
                                                                 @RequestHeader("Authorization") String jwt) throws RazorpayException {
        Orders orders = orderService.findOrdersById(orderId);
        try {
            RazorpayClient client = new RazorpayClient(apikey, apiSecret);

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", orders.getTotalDiscountedPrice()* 100);
            paymentLinkRequest.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", orders.getUser().getFirstName());
            customer.put("email", orders.getUser().getEmail());
            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("callback_url", "http://localhost:3000/payment/" + orderId);
            paymentLinkRequest.put("callback_method", "get");

            PaymentLink payment = client.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");

            PaymentLinkResponse response = new PaymentLinkResponse();
            response.setPaymentLinkId(paymentLinkId);
            response.setPaymentLinkUrl(paymentLinkUrl);

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (RazorpayException e) {
            return new ResponseEntity<PaymentLinkResponse>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<PaymentLinkResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/payment")
    public ResponseEntity<ApiResponse> redirect(@RequestParam(name = "payment_id") String paymentId,
                                                @RequestParam(name="order_id") Long orderId) throws RazorpayException {
        Orders orders = orderService.findOrdersById(orderId);
        RazorpayClient razorpay = new RazorpayClient(apikey,apiSecret);
        try{
            Payment payment = razorpay.payments.fetch(paymentId);
            if (payment.get("status").equals("captured")) {
                orders.getPaymentDetails().setPaymentId(paymentId);
                orders.getPaymentDetails().setStatus("COMPLETED");
                orders.setOrderStatus("PLACED");
                orderRepository.save(orders);
            }
            ApiResponse response = new ApiResponse();
            response.setMessage("Your Order Get Placed Successfully");
            response.setStatus(true);
            return new ResponseEntity<ApiResponse>(response,HttpStatus.ACCEPTED);
        }catch (Exception e){
            throw new RazorpayException(e.getMessage());
        }
    }

    @GetMapping("/payment/cod/{orderId}")
    public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable Long orderId , @RequestHeader("Authorization") String jwt)
    {
        Users users = userService.findUserProfileByJwt(jwt);
        Orders orders = orderService.findOrdersById(orderId);
        orders.getPaymentDetails().setPaymentMethod("Cash-On-Delivery");
        orders.getPaymentDetails().setStatus("COMPLETED");
        orders.setOrderStatus("PENDING");
        orderRepository.save(orders);
        CartItem cartItem = cartItemRepository.findByUserId(users.getUserId());
        cartItemService.removeCartItem(users.getUserId(),cartItem.getId());
        ApiResponse response = new ApiResponse();
        response.setMessage("Your Order Get Placed Successfully");
        response.setStatus(true);
        return new ResponseEntity<ApiResponse>(response,HttpStatus.ACCEPTED);
    }














}
