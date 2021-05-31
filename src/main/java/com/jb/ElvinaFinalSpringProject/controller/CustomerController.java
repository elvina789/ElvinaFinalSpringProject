package com.jb.ElvinaFinalSpringProject.controller;

import com.jb.ElvinaFinalSpringProject.Beans.Coupon;
import com.jb.ElvinaFinalSpringProject.Beans.Customer;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.Category;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.LoginCredentials;
import com.jb.ElvinaFinalSpringProject.Beans.Session;
import com.jb.ElvinaFinalSpringProject.Login.LoginManager;
import com.jb.ElvinaFinalSpringProject.security.TokenManager;
import com.jb.ElvinaFinalSpringProject.services.interfaces.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customer")
public class CustomerController {
    private final CustomerService customerService;
    private final LoginManager loginManager;
    private final TokenManager tokenManager;

    @Autowired
    public CustomerController(CustomerService customerService, LoginManager loginManager, TokenManager tokenManager) {
        this.customerService = customerService;
        this.loginManager = loginManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginCredentials credentials) {
        try {
            Session session = loginManager.login(credentials.getEmail(), credentials.getPassword(), ClientType.Customer);
            if (session != null) {
                return new ResponseEntity<>(session, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Incorrect email or password", HttpStatus.OK);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            if (tokenManager.validateToken(token, ClientType.Customer)) {
                loginManager.logout(token, ClientType.Customer);
                return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("purchase")
    public ResponseEntity<?> purchaseCoupon(@RequestHeader("Authorization") String token, @RequestBody Coupon coupon) {
        try {
            if (tokenManager.validateToken(token, ClientType.Customer)) {
                Session session = tokenManager.getTokenRecord(token);
                customerService.purchaseCoupon(session.getBeanId(), coupon);
                return new ResponseEntity<>(coupon, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("coupons")
    public ResponseEntity<?> getCustomerCoupons(@RequestHeader("Authorization") String token) {
        try {
            if (tokenManager.validateToken(token, ClientType.Customer)) {
                Session session = tokenManager.getTokenRecord(token);
                List<Coupon> customerCoupons = customerService.getCustomerCoupons(session.getBeanId());
                return new ResponseEntity<>(customerCoupons, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(name = "coupons", params = "maxPrice")
    public ResponseEntity<?> getCustomerCouponsByMaxPrice(@RequestHeader("Authorization") String token, @RequestParam double maxPrice) {
        try {
            if (tokenManager.validateToken(token, ClientType.Customer)) {
                Session session = tokenManager.getTokenRecord(token);
                List<Coupon> customerCoupons = customerService.getCustomerCoupons(session.getBeanId(), maxPrice);
                return new ResponseEntity<>(customerCoupons, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(name = "coupons", params = "category")
    public ResponseEntity<?> getCustomerCouponsByCategory(@RequestHeader("Authorization") String token, @RequestParam int categoryId) {
        try {
            if (tokenManager.validateToken(token, ClientType.Customer)) {
                Session session = tokenManager.getTokenRecord(token);
                List<Coupon> customerCoupons = customerService.getCustomerCoupons(session.getBeanId(), Category.idToCategory(categoryId));
                return new ResponseEntity<>(customerCoupons, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("details")
    public ResponseEntity<?> getCustomerDetails(@RequestHeader("Authorization") String token) {
        try {
            if (tokenManager.validateToken(token, ClientType.Customer)) {
                Session session = tokenManager.getTokenRecord(token);
                Customer customer = customerService.getCustomerDetails(session.getBeanId());
                return new ResponseEntity<>(customer, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

