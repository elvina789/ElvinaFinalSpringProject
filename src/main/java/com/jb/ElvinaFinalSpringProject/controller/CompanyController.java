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

/**
 * Controller to expose HTTP methods For company service
 */
@RestController
@RequestMapping("company")
public class CompanyController {
    private final CompanyService companyService;
    private final LoginManager loginManager;
    private final SessionManager sessionManager;

    /**
     * Constructor of the Company controller type object
     *
     * @param companyService companyService of CompanyController object
     * @param loginManager   loginManager of CompanyController object
     * @param sessionManager sessionManager of CompanyController object
     */
    @Autowired
    public CompanyController(CompanyService companyService, LoginManager loginManager, SessionManager sessionManager) {
        this.companyService = companyService;
        this.loginManager = loginManager;
        this.sessionManager = sessionManager;
    }

    /**
     * expose HTTP method to login
     *
     * @param credentials object stores credentials of the user
     * @return session and HTTP Status if succeeded to login, and if not only HTTP status
     */
    @CrossOrigin
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginCredentials credentials) {
        Session session = loginManager.login(credentials.getEmail(), credentials.getPassword(), ClientType.Company);
        if (session != null) {
            return new ResponseEntity<>(session, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Incorrect email or password", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * exposes Http method to logout
     *
     * @param token token for a session
     * @return HTTP status
     */
    @CrossOrigin
    @PostMapping("logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Company);
        loginManager.logout(token, ClientType.Company);
        return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
    }

    /**
     * exposes HTTP method to add coupon
     *
     * @param token  token for a session
     * @param coupon coupon to add
     * @return coupon and HTTP status
     */
    @CrossOrigin
    @PostMapping("coupon")
    public ResponseEntity<Coupon> addCoupon(@RequestHeader("Authorization") String token, @RequestBody Coupon coupon) {
        Session session = sessionManager.getSession(token);
        companyService.addCoupon(session.getBeanId(), coupon);
        return new ResponseEntity<>(coupon, HttpStatus.CREATED);
    }

    /**
     * expose HTTP method to update Coupon
     *
     * @param token  token for a session
     * @param coupon coupon to update
     * @return update coupon and HTTP status
     */
    @CrossOrigin
    @PutMapping("coupon")
    public ResponseEntity<Coupon> updateCoupon(@RequestHeader("Authorization") String token, @RequestBody Coupon coupon) {
        sessionManager.validateToken(token, ClientType.Company);
        Session session = sessionManager.getSession(token);
        companyService.updateCoupon(session.getBeanId(), coupon);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    /**
     * exposes HTTP method to delete coupon
     *
     * @param token token for a session
     * @param id    id of coupon to dekete
     * @return HTTP status
     */
    @CrossOrigin
    @DeleteMapping("coupon/{id}")
    public ResponseEntity<String> deleteCoupon(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        sessionManager.validateToken(token, ClientType.Company);
        Session session = sessionManager.getSession(token);
        companyService.deleteCoupon(session.getBeanId(), id);
        return new ResponseEntity<>("Successfully deleted coupon " + id, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("coupon/{id}")
    public ResponseEntity<Coupon> getCoupon(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        sessionManager.validateToken(token, ClientType.Company);
        Session session = sessionManager.getSession(token);
        Coupon coupon = companyService.getCompanyCoupon(session.getBeanId(), id);
        if (coupon == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(coupon, HttpStatus.FOUND);
        }
    }

    /**
     * exposes HTTP method to get company coupons
     *
     * @param token token for session
     * @return list of coupons and HTTP status
     */
    @CrossOrigin
    @GetMapping("coupons")
    public ResponseEntity<List<Coupon>> getCompanyCoupons(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Company);
        Session session = sessionManager.getSession(token);
        List<Coupon> coupons = companyService.getCompanyCoupons(session.getBeanId());
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    /**
     * expose Http method to get company coupons by category
     *
     * @param token      token for session
     * @param categoryId id of the category
     * @return list of coupons and HTTP status
     */
    @CrossOrigin
    @GetMapping(value = "coupons", params = "categoryId")
    public ResponseEntity<List<Coupon>> getCompanyCouponsByCategoryId(@RequestHeader("Authorization") String token, @RequestParam int categoryId) {
        sessionManager.validateToken(token, ClientType.Company);
        Session session = sessionManager.getSession(token);
        List<Coupon> coupons = companyService.getCompanyCoupons(session.getBeanId(), Category.idToCategory(categoryId));
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    /**
     * exposes HTTP method to get company coupons by max prise
     *
     * @param token    token for session
     * @param maxPrice max prise
     * @return company and HTTP status
     */
    @CrossOrigin
    @GetMapping(value = "coupons", params = "maxPrice")
    public ResponseEntity<List<Coupon>> getCompanyCouponsByMaxPrice(@RequestHeader("Authorization") String token, @RequestParam double maxPrice) {
        sessionManager.validateToken(token, ClientType.Company);
        Session session = sessionManager.getSession(token);
        List<Coupon> coupons = companyService.getCompanyCoupons(session.getBeanId(), maxPrice);
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    /**
     * expose HTTP method top get company details
     *
     * @param token token for a session
     * @return company and HTTP status
     */
    @CrossOrigin
    @GetMapping("details")
    public ResponseEntity<Company> getCompanyDetails(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Company);
        Session session = sessionManager.getSession(token);
        Company company = companyService.getCompanyDetails(session.getBeanId());
        return new ResponseEntity<>(company, HttpStatus.OK);
    }
}


