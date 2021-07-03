package com.jb.ElvinaFinalSpringProject.controller;

import com.jb.ElvinaFinalSpringProject.Beans.Company;
import com.jb.ElvinaFinalSpringProject.Beans.Customer;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.LoginCredentials;
import com.jb.ElvinaFinalSpringProject.Beans.Session;
import com.jb.ElvinaFinalSpringProject.Login.LoginManager;
import com.jb.ElvinaFinalSpringProject.scheduler.ScheduledTaskManager;
import com.jb.ElvinaFinalSpringProject.security.SessionManager;
import com.jb.ElvinaFinalSpringProject.services.interfaces.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller to expose HTTP methods For Admin service
 */

@RestController
@RequestMapping("admin")
public class AdminController {
    private final AdminService adminService;
    private final LoginManager loginManager;
    private final SessionManager sessionManager;
    private final ScheduledTaskManager scheduledTaskManager;

    /**
     * Constructor of the AdminController type object
     *
     * @param adminService         adminService of AdminController object
     * @param loginManager         loginManager of AdminController object
     * @param sessionManager       sessionManager of AdminController object
     * @param scheduledTaskManager scheduledTaskManager of AdminController object
     */
    @Autowired
    public AdminController(AdminService adminService, LoginManager loginManager, SessionManager sessionManager, ScheduledTaskManager scheduledTaskManager) {
        this.adminService = adminService;
        this.loginManager = loginManager;
        this.sessionManager = sessionManager;
        this.scheduledTaskManager = scheduledTaskManager;
    }

    /**
     * exposes HTTP method to login
     *
     * @param credentials object stores credentials of the user
     * @return session and HTTP Status if succeeded to login, and if not only HTTP status
     */
    @PostMapping("login")
    @CrossOrigin
    public ResponseEntity<?> login(@RequestBody LoginCredentials credentials) {
        Session session = loginManager.login(credentials.getEmail(), credentials.getPassword(), ClientType.Administrator);
        if (session != null) {
            return new ResponseEntity<>(session, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Incorrect email or password", HttpStatus.OK);
        }
    }

    /**
     * exposes HTTP method to logout
     *
     * @param token token for a session
     * @return HTTP status
     */
    @PostMapping("logout")
    @CrossOrigin
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Administrator);
        loginManager.logout(token, ClientType.Administrator);
        return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
    }

    /**
     * exposes HTTP method to add customer
     *
     * @param token    token for a session
     * @param customer customer to add
     * @return token amd HTTP status
     */
    @PostMapping("customer")
    @CrossOrigin
    public ResponseEntity<Customer> addCustomer(@RequestHeader("Authorization") String token, @RequestBody Customer customer) {
        sessionManager.validateToken(token, ClientType.Administrator);
        adminService.addCustomer(customer);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);

    }

    /**
     * exposes HTTP method to update customer
     *
     * @param token    token for a session
     * @param customer customer yo update
     * @return returns updated customer and HTTP status
     */
    @PutMapping("customer")
    @CrossOrigin
    public ResponseEntity<Customer> updateCustomer(@RequestHeader("Authorization") String token, @RequestBody Customer customer) {
        sessionManager.validateToken(token, ClientType.Administrator);
        adminService.updateCustomer(customer);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    /**
     * exposes HTTP method to get one customer
     *
     * @param token token for a session
     * @param id    id of customer
     * @return if customer is null returns HTTP status only, if customer is not null, returns customer and HTTP status
     */
    @GetMapping("customer/{id}")
    @CrossOrigin
    public ResponseEntity<Customer> getOneCustomer(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        sessionManager.validateToken(token, ClientType.Administrator);
        Customer customer = adminService.getOneCustomer(id);
        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(customer, HttpStatus.FOUND);
        }
    }

    /**
     * exposes HTTP method to delete customer
     *
     * @param token token for a session
     * @param id    id of customer
     * @return Http status
     */
    @DeleteMapping("customer/{id}")
    @CrossOrigin
    public ResponseEntity<String> deleteCustomer(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        sessionManager.validateToken(token, ClientType.Administrator);
        adminService.deleteCustomer(id);
        return new ResponseEntity<>("Successfully deleted customer", HttpStatus.OK);
    }

    /**
     * expose HTTP method to get all customers
     *
     * @param token token for a session
     * @return list of companies and HTTP status
     */
    @GetMapping("customers")
    @CrossOrigin
    public ResponseEntity<List<Customer>> getAllCustomers(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Administrator);
        List<Customer> companies = adminService.getAllCustomers();
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    /**
     * exposes HTTP method to add company
     *
     * @param token   token for a session
     * @param company company to add
     * @return company and HTTP status
     */
    @PostMapping("company")
    @CrossOrigin
    public ResponseEntity<Company> addCompany(@RequestHeader("Authorization") String token, @RequestBody Company company) {
        sessionManager.validateToken(token, ClientType.Administrator);
        adminService.addCompany(company);
        return new ResponseEntity<>(company, HttpStatus.CREATED);
    }

    /**
     * exposes HTTP method to update company
     *
     * @param token   token for a session
     * @param company company to update
     * @return returns company and HTTP status
     */
    @PutMapping("company")
    @CrossOrigin
    public ResponseEntity<Company> updateCompany(@RequestHeader("Authorization") String token, @RequestBody Company company) {
        sessionManager.validateToken(token, ClientType.Administrator);
        adminService.updateCompany(company);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    /**
     * exposes HTTP method to get one company
     *
     * @param token token for a session
     * @param id    id of the company
     * @return if companmy is null returns HTTP status only, if company not null returns company and HTTP status
     */
    @GetMapping("company/{id}")
    @CrossOrigin
    public ResponseEntity<Company> getOneCompany(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        sessionManager.validateToken(token, ClientType.Administrator);
        Company company = adminService.getOneCompany(id);
        if (company == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(company, HttpStatus.FOUND);
        }
    }

    /**
     * exposes HTTP method to delete company
     *
     * @param token token for session
     * @param id    id of the company
     * @return HTTP status
     */
    @DeleteMapping("company/{id}")
    @CrossOrigin
    public ResponseEntity<String> deleteCompany(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        sessionManager.validateToken(token, ClientType.Administrator);
        adminService.deleteCompany(id);
        return new ResponseEntity<>("Company successfully deleted", HttpStatus.OK);
    }

    /**
     * expose HTTP method to get all companies
     *
     * @param token token for a session
     * @return list of companies and HTTP status
     */
    @DeleteMapping("companies")
    @CrossOrigin
    public ResponseEntity<List<Company>> getAllCompanies(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Administrator);
        List<Company> companies = adminService.getAllCompanies();
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    /**
     * expose HTTP method to start cleaning job
     *
     * @param token token for a session
     * @return HTTP status
     */
    @PostMapping("cleaning/start")
    @CrossOrigin
    public ResponseEntity<String> startCleaningJob(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Administrator);
        scheduledTaskManager.startExpiredCouponDailyClean();
        return new ResponseEntity<>("Cleaning job started", HttpStatus.OK);
    }

    /**
     * expose HTTP method to stop cleaning job
     *
     * @param token token for a sesion
     * @return token for a session
     */
    @PostMapping("cleaning/stop")
    @CrossOrigin
    public ResponseEntity<String> stopCleaningJob(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Administrator);
        scheduledTaskManager.stopExpiredCouponDailyClean();
        return new ResponseEntity<>("Cleaning job stopped", HttpStatus.OK);
    }
}
