package com.jb.ElvinaFinalSpringProject.controller;

import com.jb.ElvinaFinalSpringProject.Beans.Company;
import com.jb.ElvinaFinalSpringProject.Beans.Customer;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.LoginCredentials;
import com.jb.ElvinaFinalSpringProject.Beans.Session;
import com.jb.ElvinaFinalSpringProject.Login.LoginManager;
import com.jb.ElvinaFinalSpringProject.scheduler.ScheduledTaskManager;
import com.jb.ElvinaFinalSpringProject.security.TokenManager;
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
    private final TokenManager tokenManager;
    private final ScheduledTaskManager scheduledTaskManager;

    @Autowired
    public AdminController(AdminService adminService, LoginManager loginManager, TokenManager tokenManager, ScheduledTaskManager scheduledTaskManager) {
        this.adminService = adminService;
        this.loginManager = loginManager;
        this.tokenManager = tokenManager;
        this.scheduledTaskManager = scheduledTaskManager;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginCredentials credentials) {
        try {
            Session session = loginManager.login(credentials.getEmail(), credentials.getPassword(), ClientType.Administrator);
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
            if (tokenManager.validateToken(token, ClientType.Administrator)) {
                loginManager.logout(token, ClientType.Administrator);
                return new ResponseEntity<>("Successfully logged out", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("customer")
    public ResponseEntity<?> addCustomer(@RequestHeader("Authorization") String token, @RequestBody Customer customer) {
        try {
            if (tokenManager.validateToken(token, ClientType.Administrator)) {
                adminService.addCustomer(customer);
                return new ResponseEntity<>(customer, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("customer")
    public ResponseEntity<?> updateCustomer(@RequestHeader("Authorization") String token, @RequestBody Customer customer) {
        try {
            if (tokenManager.validateToken(token, ClientType.Administrator)) {
                adminService.updateCustomer(customer);
                return new ResponseEntity<>(customer, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("customer/{id}")
    public ResponseEntity<?> getOneCustomer(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        try {
            if (tokenManager.validateToken(token, ClientType.Administrator)) {
                Customer customer = adminService.getOneCustomer(id);
                if (customer == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                } else {
                    return new ResponseEntity<>(customer, HttpStatus.FOUND);
                }
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("customer/{id}")
    public ResponseEntity<?> deleteCustomer(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        try {
            if (tokenManager.validateToken(token, ClientType.Administrator)) {
                adminService.deleteCustomer(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("customers")
    public ResponseEntity<?> getAllCustomers(@RequestHeader("Authorization") String token) {
        try {
            if (tokenManager.validateToken(token, ClientType.Administrator)) {
                List<Customer> companies = adminService.getAllCustomers();
                return new ResponseEntity<>(companies, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("company")
    public ResponseEntity<?> addCompany(@RequestHeader("Authorization") String token, @RequestBody Company company) {
        try {
            if (tokenManager.validateToken(token, ClientType.Administrator)) {
                adminService.addCompany(company);
                return new ResponseEntity<>(company, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("company")
    public ResponseEntity<?> updateCompany(@RequestHeader("Authorization") String token, @RequestBody Company company) {
        try {
            if (tokenManager.validateToken(token, ClientType.Administrator)) {
                adminService.updateCompany(company);
                return new ResponseEntity<>(company, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("company/{id}")
    public ResponseEntity<?> getOneCompany(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        try {
            if (tokenManager.validateToken(token, ClientType.Administrator)) {
                Company company = adminService.getOneCompany(id);
                if (company == null) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                } else {
                    return new ResponseEntity<>(company, HttpStatus.FOUND);
                }
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("company/{id}")
    public ResponseEntity<?> deleteCompany(@RequestHeader("Authorization") String token, @PathVariable Integer id) {
        try {
            if (tokenManager.validateToken(token, ClientType.Administrator)) {
                adminService.deleteCompany(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("companies")
    public ResponseEntity<?> getAllCompanies(@RequestHeader("Authorization") String token) {
        try {
            if (tokenManager.validateToken(token, ClientType.Administrator)) {
                List<Company> companies = adminService.getAllCompanies();
                return new ResponseEntity<>(companies, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("cleaning/start")
    public ResponseEntity<?> startCleaningJob(@RequestHeader("Authorization") String token) {
        try {
            if (tokenManager.validateToken(token, ClientType.Administrator)) {
                scheduledTaskManager.startExpiredCouponDailyClean();
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("cleaning/stop")
    public ResponseEntity<?> stopCleaningJob(@RequestHeader("Authorization") String token) {
        try {
            if (tokenManager.validateToken(token, ClientType.Administrator)) {
                scheduledTaskManager.stopExpiredCouponDailyClean();
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
            }
        } catch (Throwable t) {
            return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
