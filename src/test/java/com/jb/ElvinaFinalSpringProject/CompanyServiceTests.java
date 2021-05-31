package com.jb.ElvinaFinalSpringProject;

import com.jb.ElvinaFinalSpringProject.Beans.Company;
import com.jb.ElvinaFinalSpringProject.Beans.Coupon;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.Category;
import com.jb.ElvinaFinalSpringProject.Beans.LoginCredentials;
import com.jb.ElvinaFinalSpringProject.Beans.TokenRecord;
import com.jb.ElvinaFinalSpringProject.Repositories.CompanyRepository;
import com.jb.ElvinaFinalSpringProject.Repositories.CouponRepository;
import com.jb.ElvinaFinalSpringProject.controller.AdminController;
import com.jb.ElvinaFinalSpringProject.controller.CompanyController;
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
public class CompanyServiceTests {
    private final CompanyRepository companyRepository;
    private final CouponRepository couponRepository;
    private final CompanyController companyController;
    private final AdminController adminController;

    // Common data must be static
    private static TokenRecord tokenRecord;
    private static Company company;
    private static final List<Coupon> couponTemplates = new ArrayList<>();

    @Autowired
    public CompanyServiceTests(CompanyRepository companyRepository, CouponRepository couponRepository, CompanyController companyController, AdminController adminController) {
        this.companyRepository = companyRepository;
        this.couponRepository = couponRepository;
        this.companyController = companyController;
        this.adminController = adminController;
    }

    @BeforeAll
    void initTests() {
        LoginCredentials credentials = LoginCredentials.builder().email("admin@admin.com").password("admin").build();
        ResponseEntity<?> response = adminController.login(credentials);
        TokenRecord adminTokenRecord = (TokenRecord) response.getBody();
        company = Company.builder().name("Company1").email("company1@company1.com").password("company1").build();
        adminController.addCompany(adminTokenRecord.getToken(), company);
        couponTemplates.add(Coupon.builder().companyId(company.getId()).categoryId(Category.Electricity.getId()).title("Coupon1").description("Description1").startDate(DateTime.now().toDate()).endDate(DateTime.now().plusDays(1).toDate()).amount(15).price(120.320).image("image1").build());
        couponTemplates.add(Coupon.builder().companyId(company.getId()).categoryId(Category.Restaurant.getId()).title("Coupon2").description("Description2").startDate(DateTime.now().toDate()).endDate(DateTime.now().plusDays(1).toDate()).amount(120).price(1020.30).image("image2").build());
        couponTemplates.add(Coupon.builder().companyId(company.getId()).categoryId(Category.Vacation.getId()).title("Coupon3").description("Description3").startDate(DateTime.now().toDate()).endDate(DateTime.now().plusDays(1).toDate()).amount(120).price(1020.300).image("image3").build());
    }

    @AfterAll
    void clearAfterTests() {
        companyRepository.deleteAll();
        couponRepository.deleteAll();
    }

    @Test
    @Order(1)
    void loginTest() {
        LoginCredentials credentials = LoginCredentials.builder()
                .email(company.getEmail())
                .password(company.getPassword())
                .build();
        ResponseEntity<?> response = companyController.login(credentials);
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        tokenRecord = (TokenRecord) response.getBody();
        Assert.notNull(tokenRecord, "Admin token record returned null");
    }

    @Test
    @Order(2)
    void addCouponsTest() {
        for (Coupon coupon : couponTemplates) {
            ResponseEntity<?> response = companyController.addCoupon(tokenRecord.getToken(), coupon);
            Assert.isTrue(response.getStatusCode().equals(HttpStatus.CREATED), "Status returned not as expected");
            log.info("Created coupon {}", coupon);
        }
    }

    @Test
    @Order(3)
    void getCouponsByCategoryTest() {
        ResponseEntity<?> response = companyController.getCompanyCouponsByCategory(tokenRecord.getToken(), Category.Electricity.getId());
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        List<Coupon> coupons = (List<Coupon>) response.getBody();
        Assert.isTrue(coupons.size() == 1, "Incorrect number of coupons");
        Assert.isTrue(coupons.get(0).getCategoryId() == Category.Electricity.getId(), "Incorrect category id returned");
    }

    @Test
    @Order(4)
    void getCouponsByPriceTest() {
        double maxPrice = 1000.0;
        ResponseEntity<?> response = companyController.getCompanyCouponsByMaxPrice(tokenRecord.getToken(), maxPrice);
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        List<Coupon> coupons = (List<Coupon>) response.getBody();
        Assert.notNull(coupons, "Got null as result");
        Assert.isTrue(coupons.size() == 1, "Incorrect number of coupons");
        Assert.isTrue(coupons.get(0).getPrice() <= 1000.0, "Incorrect price returned");
    }

    @Test
    @Order(5)
    void getAllCoupons() {
        ResponseEntity<?> response = companyController.getCompanyCoupons(tokenRecord.getToken());
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        List<Coupon> coupons = (List<Coupon>) response.getBody();
        Assert.notNull(coupons, "Got null as result");
        Assert.isTrue(coupons.size() == 3, "Incorrect number of coupons");
        Assert.isTrue(company.getId() == coupons.get(0).getCompanyId(), "Incorrect company id returned in coupon");
    }

    @Test
    @Order(6)
    void deleteCouponTest() {
        int id = couponTemplates.get(0).getId();
        ResponseEntity<?> deleteResponse = companyController.deleteCoupon(tokenRecord.getToken(), id);
        Assert.isTrue(deleteResponse.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        ResponseEntity<?> getCouponsResponse = companyController.getCompanyCoupons(tokenRecord.getToken());
        Assert.isTrue(getCouponsResponse.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        List<Coupon> coupons = (List<Coupon>) getCouponsResponse.getBody();
        boolean notFound = coupons.stream().noneMatch(e -> e.getId() == id);
        Assert.isTrue(notFound, "Coupon found after deleting");
    }

    @Test
    @Order(7)
    void updateCouponTest() {
        Coupon coupon = couponTemplates.get(1);
        int newAmount = 400;
        coupon.setAmount(newAmount);
        ResponseEntity<?> updateResponse = companyController.updateCoupon(tokenRecord.getToken(), coupon);
        Assert.isTrue(updateResponse.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        ResponseEntity<?> getCouponsResponse = companyController.getCompanyCoupons(tokenRecord.getToken());
        Assert.isTrue(getCouponsResponse.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        List<Coupon> coupons = (List<Coupon>) getCouponsResponse.getBody();
        boolean anyMatch = coupons.stream().anyMatch(e -> e.getId() == coupon.getId() && e.getAmount() == newAmount);
        Assert.isTrue(anyMatch, "Amount not updated for coupon");
    }

    @Test
    @Order(8)
    void companyDetailsTest() {
        ResponseEntity<?> response = companyController.getCompanyDetails(tokenRecord.getToken());
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        Company companyDetails = (Company) response.getBody();
        Assert.isTrue(company.equals(companyDetails), "Company details returned incorrectly");
    }

    @Test
    @Order(9)
    void logOut() {
        ResponseEntity<?> response = companyController.logout(tokenRecord.getToken());
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
    }
}