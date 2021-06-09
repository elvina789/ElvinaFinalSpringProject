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

@RestController
@RequestMapping("admin")
public class AdminController {
    private final AdminService adminService;
    private final LoginManager loginManager;
    private final SessionManager sessionManager;
    private final ScheduledTaskManager scheduledTaskManager;

    @Autowired
    public AdminController(AdminService adminService, LoginManager loginManager, SessionManager sessionManager, ScheduledTaskManager scheduledTaskManager) {
        this.adminService = adminService;
        this.loginManager = loginManager;
        this.sessionManager = sessionManager;
        this.scheduledTaskManager = scheduledTaskManager;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginCredentials credentials) {
        Session session = loginManager.login(credentials.getEmail(), credentials.getPassword(), ClientType.Administrator);
        if (session != null) {
            return new ResponseEntity<>(session, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Incorrect email or password", HttpStatus.OK);
        }
    }

    @PostMapping("logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Administrator);
        loginManager.logout(token, ClientType.Administrator);
        return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
    }

    @PostMapping("customer")
    public ResponseEntity<Customer> addCustomer(@RequestHeader("Authorization") String token, @RequestBody Customer customer) {
        sessionManager.validateToken(token, ClientType.Administrator);
        adminService.addCustomer(customer);
        return new ResponseEntity<>(customer, HttpStatus.CREATED);

    }

    @PutMapping("customer")
    public ResponseEntity<Customer> updateCustomer(@RequestHeader("Authorization") String token, @RequestBody Customer customer) {
        sessionManager.validateToken(token, ClientType.Administrator);
        adminService.updateCustomer(customer);
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("customer/{id}")
    public ResponseEntity<Customer> getOneCustomer(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        sessionManager.validateToken(token, ClientType.Administrator);
        Customer customer = adminService.getOneCustomer(id);
        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(customer, HttpStatus.FOUND);
        }
    }

    @DeleteMapping("customer/{id}")
    public ResponseEntity<String> deleteCustomer(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        sessionManager.validateToken(token, ClientType.Administrator);
        adminService.deleteCustomer(id);
        return new ResponseEntity<>("Successfully deleted customer", HttpStatus.OK);
    }

    @DeleteMapping("customers")
    public ResponseEntity<List<Customer>> getAllCustomers(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Administrator);
        List<Customer> companies = adminService.getAllCustomers();
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @PostMapping("company")
    public ResponseEntity<Company> addCompany(@RequestHeader("Authorization") String token, @RequestBody Company company) {
        sessionManager.validateToken(token, ClientType.Administrator);
        adminService.addCompany(company);
        return new ResponseEntity<>(company, HttpStatus.CREATED);
    }

    @PutMapping("company")
    public ResponseEntity<Company> updateCompany(@RequestHeader("Authorization") String token, @RequestBody Company company) {
        sessionManager.validateToken(token, ClientType.Administrator);
        adminService.updateCompany(company);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @GetMapping("company/{id}")
    public ResponseEntity<Company> getOneCompany(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        sessionManager.validateToken(token, ClientType.Administrator);
        Company company = adminService.getOneCompany(id);
        if (company == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(company, HttpStatus.FOUND);
        }
    }

    @DeleteMapping("company/{id}")
    public ResponseEntity<String> deleteCompany(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        sessionManager.validateToken(token, ClientType.Administrator);
        adminService.deleteCompany(id);
        return new ResponseEntity<>("Company successfully deleted", HttpStatus.OK);
    }

    @DeleteMapping("companies")
    public ResponseEntity<List<Company>> getAllCompanies(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Administrator);
        List<Company> companies = adminService.getAllCompanies();
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @PostMapping("cleaning/start")
    public ResponseEntity<String> startCleaningJob(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Administrator);
        scheduledTaskManager.startExpiredCouponDailyClean();
        return new ResponseEntity<>("Cleaning job started", HttpStatus.OK);
    }

    @PostMapping("cleaning/stop")
    public ResponseEntity<String> stopCleaningJob(@RequestHeader("Authorization") String token) {
        sessionManager.validateToken(token, ClientType.Administrator);
        scheduledTaskManager.stopExpiredCouponDailyClean();
        return new ResponseEntity<>("Cleaning job stopped", HttpStatus.OK);
    }
}
