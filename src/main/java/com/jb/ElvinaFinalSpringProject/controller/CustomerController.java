package com.jb.ElvinaFinalSpringProject.controller;

import com.jb.ElvinaFinalSpringProject.Beans.Coupon;
import com.jb.ElvinaFinalSpringProject.Beans.Customer;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.Category;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.LoginCredentials;
import com.jb.ElvinaFinalSpringProject.Beans.Session;
import com.jb.ElvinaFinalSpringProject.Login.LoginManager;
import com.jb.ElvinaFinalSpringProject.security.SessionManager;
import com.jb.ElvinaFinalSpringProject.services.interfaces.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller to expose HTTP methods For customer service
 */
@RestController
@RequestMapping("customer")
public class CustomerController {
    private final CustomerService customerService;
    private final LoginManager loginManager;
    private final SessionManager sessionManager;

    /**
     * Constructor of the Customer controller type object
     *
     * @param customerService customerService of CustomerController object
     * @param loginManager    loginManager of CustomerController object
     * @param sessionManager  sessionManager of CustomerController object
     */
    @Autowired
    public CustomerController(CustomerService customerService, LoginManager loginManager, SessionManager sessionManager) {
        this.customerService = customerService;
        this.loginManager = loginManager;
        this.sessionManager = sessionManager;
    }

    /**
     * expose HTTP method to login
     *
     * @param credentials object stores credentials of the user
     * @return session and HTTP Status if succeeded to login, and if not only HTTP status
     */
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginCredentials credentials) {
        Session session = loginManager.login(credentials.getEmail(), credentials.getPassword(), ClientType.Customer);
        if (session != null) {
            return new ResponseEntity<>(session, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Incorrect email or password", HttpStatus.OK);
        }
    }

    /**
     * exposes Http method to logout
     *
     * @param token token for a session
     * @return HTTP status
     */
    @PostMapping("logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Customer);
        loginManager.logout(token, ClientType.Customer);
        return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
    }

    /**
     * exposes HTTP method to purchase coupon
     *
     * @param token  token for  a session
     * @param coupon coupon to purchase
     * @return coupon and HTTP status
     */
    @PostMapping("purchase")
    public ResponseEntity<Coupon> purchaseCoupon(@RequestHeader("Authorization") String token, @RequestBody Coupon coupon) {
        sessionManager.validateToken(token, ClientType.Customer);
        Session session = sessionManager.getSession(token);
        customerService.purchaseCoupon(session.getBeanId(), coupon);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    /**
     * exposes HTTP method to get customer coupons
     *
     * @param token token for a session
     * @return return  list of coupons and HTTP status
     */
    @GetMapping("coupons")
    public ResponseEntity<List<Coupon>> getCustomerCoupons(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Customer);
        Session session = sessionManager.getSession(token);
        List<Coupon> customerCoupons = customerService.getCustomerCoupons(session.getBeanId());
        return new ResponseEntity<>(customerCoupons, HttpStatus.OK);
    }

    /**
     * exposes HTTP method to get customer coupons by max price
     *
     * @param token    token for a session
     * @param maxPrice max price of coupon
     * @return list of coupons and HTTP status
     */
    @GetMapping(name = "coupons", params = "maxPrice")
    public ResponseEntity<List<Coupon>> getCustomerCouponsByMaxPrice(@RequestHeader("Authorization") String token, @RequestParam double maxPrice) {
        sessionManager.validateToken(token, ClientType.Customer);
        Session session = sessionManager.getSession(token);
        List<Coupon> customerCoupons = customerService.getCustomerCoupons(session.getBeanId(), maxPrice);
        return new ResponseEntity<>(customerCoupons, HttpStatus.OK);
    }

    /**
     * expose HTTP method to get customer Coupons by category
     *
     * @param token      token for a session
     * @param categoryId id of the category
     * @return list of  coupons and HTTP status
     */
    @GetMapping(name = "coupons", params = "category")
    public ResponseEntity<List<Coupon>> getCustomerCouponsByCategory(@RequestHeader("Authorization") String token, @RequestParam int categoryId) {
        sessionManager.validateToken(token, ClientType.Customer);
        Session session = sessionManager.getSession(token);
        List<Coupon> customerCoupons = customerService.getCustomerCoupons(session.getBeanId(), Category.idToCategory(categoryId));
        return new ResponseEntity<>(customerCoupons, HttpStatus.OK);
    }

    /**
     * expose HTTP method to get customer details
     *
     * @param token token for a session
     * @return customer and HTTP status
     */
    @GetMapping("details")
    public ResponseEntity<Customer> getCustomerDetails(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Customer);
        Session session = sessionManager.getSession(token);
        Customer customer = customerService.getCustomerDetails(session.getBeanId());
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
}

