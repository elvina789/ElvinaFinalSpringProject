package com.jb.ElvinaFinalSpringProject;

import com.jb.ElvinaFinalSpringProject.Beans.*;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.Category;
import com.jb.ElvinaFinalSpringProject.Repositories.CompanyRepository;
import com.jb.ElvinaFinalSpringProject.Repositories.CouponRepository;
import com.jb.ElvinaFinalSpringProject.Repositories.CustomerRepository;
import com.jb.ElvinaFinalSpringProject.controller.AdminController;
import com.jb.ElvinaFinalSpringProject.controller.CompanyController;
import com.jb.ElvinaFinalSpringProject.controller.CustomerController;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerServiceTest {
    private final CouponRepository couponRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final AdminController adminController;
    private final CompanyController companyController;
    private final CustomerController customerController;

    private static Company company;
    private static Customer customer;
    private static List<Coupon> couponTemplates = new ArrayList<>();
    private static TokenRecord tokenRecord;

    @Autowired
    public CustomerServiceTest(CouponRepository couponRepository, CompanyRepository companyRepository, CustomerRepository customerRepository, AdminController adminController, CompanyController companyController, CustomerController customerController) {
        this.couponRepository = couponRepository;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.adminController = adminController;
        this.companyController = companyController;
        this.customerController = customerController;
    }

    @BeforeAll
    void initTests() {
        ResponseEntity<?> adminLoginResponse = adminController.login(LoginCredentials.builder().email("admin@admin.com").password("admin").build());
        TokenRecord adminTokenRecord = (TokenRecord) adminLoginResponse.getBody();
        company = Company.builder().name("Company1").email("company1@company1.com").password("company1").build();
        customer = Customer.builder().firstName("Customer1Name").lastName("Customer1LastName").email("customer1@customer1.com").password("customer1").build();
        adminController.addCompany(adminTokenRecord.getToken(), company);
        adminController.addCustomer(adminTokenRecord.getToken(), customer);
        couponTemplates.add(Coupon.builder().companyId(company.getId()).categoryId(Category.Electricity.getId()).title("Coupon1").description("Description1").startDate(DateTime.now().toDate()).endDate(DateTime.now().plusDays(1).toDate()).amount(15).price(120.320).image("image1").build());
        couponTemplates.add(Coupon.builder().companyId(company.getId()).categoryId(Category.Restaurant.getId()).title("Coupon2").description("Description2").startDate(DateTime.now().toDate()).endDate(DateTime.now().plusDays(1).toDate()).amount(120).price(1020.30).image("image2").build());
        couponTemplates.add(Coupon.builder().companyId(company.getId()).categoryId(Category.Vacation.getId()).title("Coupon3").description("Description3").startDate(DateTime.now().toDate()).endDate(DateTime.now().plusDays(1).toDate()).amount(120).price(1020.300).image("image3").build());
        ResponseEntity<?> companyLoginResponse = companyController.login(LoginCredentials.builder().email(company.getEmail()).password(company.getPassword()).build());
        TokenRecord companyTokenRecord = (TokenRecord) companyLoginResponse.getBody();
        for (Coupon coupon : couponTemplates) {
            companyController.addCoupon(companyTokenRecord.getToken(), coupon);
        }
    }

    @AfterAll
    void clearAfterTests() {
        couponRepository.deleteAll();
        companyRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    @Order(1)
    void loginTest() {
        LoginCredentials credentials = LoginCredentials.builder()
                .email(customer.getEmail())
                .password(customer.getPassword())
                .build();
        ResponseEntity<?> response = customerController.login(credentials);
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        tokenRecord = (TokenRecord) response.getBody();
        Assert.notNull(tokenRecord, "Admin token record returned null");
    }

    @Test
    @Order(1)
    void loginTestIncorrectCredentials() {
        LoginCredentials credentials = LoginCredentials.builder()
                .email(customer.getEmail() + "123")
                .password(customer.getPassword() + "123")
                .build();
        ResponseEntity<?> response = customerController.login(credentials);
        log.info("Returned response status {}", response.getStatusCode());
        log.info("Returned response body {}", response.getBody());
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        Assert.isTrue("Incorrect email or password".equals(response.getBody()));
    }

    @Test
    @Order(2)
    void getCustomerDetailsTest() {
        ResponseEntity<?> response = customerController.getCustomerDetails(tokenRecord.getToken());
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        Customer customerDetails = (Customer) response.getBody();
        Assert.isTrue(customer.equals(customerDetails), "Customer details returned are incorrect");
    }

    @Test
    @Order(3)
    void purchaseCouponTest() {
        ResponseEntity<?> responsePurchase1 = customerController.purchaseCoupon(tokenRecord.getToken(), couponTemplates.get(0));
        Assert.isTrue(responsePurchase1.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        ResponseEntity<?> responsePurchase2 = customerController.purchaseCoupon(tokenRecord.getToken(), couponTemplates.get(1));
        Assert.isTrue(responsePurchase2.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
    }

    @Test
    @Order(4)
    void getAllCustomerCoupons() {
        ResponseEntity<?> response = customerController.getCustomerCoupons(tokenRecord.getToken());
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        List<Coupon> coupons = (List<Coupon>) response.getBody();
        Assert.notNull(coupons, "Customer coupons returned as null");
        Assert.isTrue(coupons.size() == 2, "Number of coupons returned incorrect");
    }

    @Test
    @Order(5)
    void getCustomerCouponsByCategory() {
        ResponseEntity<?> response = customerController.getCustomerCouponsByCategory(tokenRecord.getToken(), Category.Electricity.getId());
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        List<Coupon> coupons = (List<Coupon>) response.getBody();
        Assert.notNull(coupons, "Customer coupons returned as null");
        Assert.isTrue(coupons.size() == 1, "Number of coupons returned incorrect");
        Assert.isTrue(coupons.get(0).getCategoryId() == Category.Electricity.getId(), "Incorrect category id returned");
    }

    @Test
    @Order(6)
    void getCustomerCouponsByMaxPrice() {
        double maxPrice = 1000.0;
        ResponseEntity<?> response = customerController.getCustomerCouponsByMaxPrice(tokenRecord.getToken(), maxPrice);
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        List<Coupon> coupons = (List<Coupon>) response.getBody();
        Assert.notNull(coupons, "Customer coupons returned as null");
        Assert.notNull(coupons, "Customer coupons returned as null");
        Assert.isTrue(coupons.size() == 1, "Number of coupons returned incorrect");
        Assert.isTrue(coupons.get(0).getPrice() <= maxPrice, "Category with incorrect price returned");
    }

    @Test
    @Order(7)
    void logOut() {
        ResponseEntity<?> response = customerController.logout(tokenRecord.getToken());
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
    }
}
