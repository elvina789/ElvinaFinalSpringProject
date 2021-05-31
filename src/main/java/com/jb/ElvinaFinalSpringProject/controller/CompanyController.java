package com.jb.ElvinaFinalSpringProject.controller;

import com.jb.ElvinaFinalSpringProject.Beans.Company;
import com.jb.ElvinaFinalSpringProject.Beans.Coupon;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.Category;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.LoginCredentials;
import com.jb.ElvinaFinalSpringProject.Beans.TokenRecord;
import com.jb.ElvinaFinalSpringProject.Login.LoginManager;
import com.jb.ElvinaFinalSpringProject.security.TokenManager;
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
    private final TokenManager tokenManager;

    @Autowired
    public CompanyController(CompanyService companyService, LoginManager loginManager, TokenManager tokenManager) {
        this.companyService = companyService;
        this.loginManager = loginManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginCredentials credentials) {
        try {
            TokenRecord tokenRecord = loginManager.login(credentials.getEmail(), credentials.getPassword(), ClientType.Company);
            return new ResponseEntity<>(tokenRecord, HttpStatus.OK);
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            if (tokenManager.validateToken(token, ClientType.Company)) {
                loginManager.logout(token, ClientType.Company);
                return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("coupon")
    public ResponseEntity<?> addCoupon(@RequestHeader("Authorization") String token, @RequestBody Coupon coupon) {
        try {
            if (tokenManager.validateToken(token, ClientType.Company)) {
                TokenRecord tokenRecord = tokenManager.getTokenRecord(token);
                companyService.addCoupon(tokenRecord.getBeanId(), coupon);
                return new ResponseEntity<>(coupon, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("coupon")
    public ResponseEntity<?> updateCoupon(@RequestHeader("Authorization") String token, @RequestBody Coupon coupon) {
        try {
            if (tokenManager.validateToken(token, ClientType.Company)) {
                TokenRecord tokenRecord = tokenManager.getTokenRecord(token);
                companyService.updateCoupon(tokenRecord.getBeanId(), coupon);
                return new ResponseEntity<>(coupon, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("coupon/{id}")
    public ResponseEntity<?> deleteCoupon(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        try {
            if (tokenManager.validateToken(token, ClientType.Company)) {
                TokenRecord tokenRecord = tokenManager.getTokenRecord(token);
                companyService.deleteCoupon(tokenRecord.getBeanId(), id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("coupons")
    public ResponseEntity<?> getCompanyCoupons(@RequestHeader("Authorization") String token) {
        try {
            if (tokenManager.validateToken(token, ClientType.Company)) {
                TokenRecord tokenRecord = tokenManager.getTokenRecord(token);
                List<Coupon> coupons = companyService.getCompanyCoupons(tokenRecord.getBeanId());
                return new ResponseEntity<>(coupons, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(name = "coupons", params = "categoryId")
    public ResponseEntity<?> getCompanyCouponsByCategory(@RequestHeader("Authorization") String token, @RequestParam int categoryId) {
        try {
            if (tokenManager.validateToken(token, ClientType.Company)) {
                TokenRecord tokenRecord = tokenManager.getTokenRecord(token);
                List<Coupon> coupons = companyService.getCompanyCoupons(tokenRecord.getBeanId(), Category.idToCategory(categoryId));
                return new ResponseEntity<>(coupons, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "coupons", params = "maxPrice")
    public ResponseEntity<?> getCompanyCouponsByMaxPrice(@RequestHeader("Authorization") String token, @RequestParam double maxPrice) {
        try {
            if (tokenManager.validateToken(token, ClientType.Company)) {
                TokenRecord tokenRecord = tokenManager.getTokenRecord(token);
                List<Coupon> coupons = companyService.getCompanyCoupons(tokenRecord.getBeanId(), maxPrice);
                return new ResponseEntity<>(coupons, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("details")
    public ResponseEntity<?> getCompanyDetails(@RequestHeader("Authorization") String token) {
        try {
            if (tokenManager.validateToken(token, ClientType.Company)) {
                TokenRecord tokenRecord = tokenManager.getTokenRecord(token);
                Company company = companyService.getCompanyDetails(tokenRecord.getBeanId());
                return new ResponseEntity<>(company, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


