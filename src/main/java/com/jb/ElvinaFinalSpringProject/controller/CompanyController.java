package com.jb.ElvinaFinalSpringProject.controller;

import com.jb.ElvinaFinalSpringProject.Beans.Company;
import com.jb.ElvinaFinalSpringProject.Beans.Coupon;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.Category;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.LoginCredentials;
import com.jb.ElvinaFinalSpringProject.Beans.Session;
import com.jb.ElvinaFinalSpringProject.Login.LoginManager;
import com.jb.ElvinaFinalSpringProject.security.SessionManager;
import com.jb.ElvinaFinalSpringProject.services.interfaces.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("company")
public class CompanyController {
    private final CompanyService companyService;
    private final LoginManager loginManager;
    private final SessionManager sessionManager;

    @Autowired
    public CompanyController(CompanyService companyService, LoginManager loginManager, SessionManager sessionManager) {
        this.companyService = companyService;
        this.loginManager = loginManager;
        this.sessionManager = sessionManager;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginCredentials credentials) {
        Session session = loginManager.login(credentials.getEmail(), credentials.getPassword(), ClientType.Company);
        if (session != null) {
            return new ResponseEntity<>(session, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Incorrect email or password", HttpStatus.OK);
        }
    }

    @PostMapping("logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Company);
        loginManager.logout(token, ClientType.Company);
        return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
    }

    @PostMapping("coupon")
    public ResponseEntity<Coupon> addCoupon(@RequestHeader("Authorization") String token, @RequestBody Coupon coupon) {
        Session session = sessionManager.getSession(token);
        companyService.addCoupon(session.getBeanId(), coupon);
        return new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    @PutMapping("coupon")
    public ResponseEntity<Coupon> updateCoupon(@RequestHeader("Authorization") String token, @RequestBody Coupon coupon) {
        sessionManager.validateToken(token, ClientType.Company);
        Session session = sessionManager.getSession(token);
        companyService.updateCoupon(session.getBeanId(), coupon);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    @DeleteMapping("coupon/{id}")
    public ResponseEntity<String> deleteCoupon(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        sessionManager.validateToken(token, ClientType.Company);
        Session session = sessionManager.getSession(token);
        companyService.deleteCoupon(session.getBeanId(), id);
        return new ResponseEntity<>("Successfully deleted coupon " + id, HttpStatus.OK);
    }

    @GetMapping("coupons")
    public ResponseEntity<List<Coupon>> getCompanyCoupons(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Company);
        Session session = sessionManager.getSession(token);
        List<Coupon> coupons = companyService.getCompanyCoupons(session.getBeanId());
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    @GetMapping(name = "coupons", params = "categoryId")
    public ResponseEntity<List<Coupon>> getCompanyCouponsByCategory(@RequestHeader("Authorization") String token, @RequestParam int categoryId) {
        sessionManager.validateToken(token, ClientType.Company);
        Session session = sessionManager.getSession(token);
        List<Coupon> coupons = companyService.getCompanyCoupons(session.getBeanId(), Category.idToCategory(categoryId));
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    @GetMapping(value = "coupons", params = "maxPrice")
    public ResponseEntity<List<Coupon>> getCompanyCouponsByMaxPrice(@RequestHeader("Authorization") String token, @RequestParam double maxPrice) {
        sessionManager.validateToken(token, ClientType.Company);
        Session session = sessionManager.getSession(token);
        List<Coupon> coupons = companyService.getCompanyCoupons(session.getBeanId(), maxPrice);
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    @GetMapping("details")
    public ResponseEntity<Company> getCompanyDetails(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Company);
        Session session = sessionManager.getSession(token);
        Company company = companyService.getCompanyDetails(session.getBeanId());
        return new ResponseEntity<>(company, HttpStatus.OK);
    }
}


