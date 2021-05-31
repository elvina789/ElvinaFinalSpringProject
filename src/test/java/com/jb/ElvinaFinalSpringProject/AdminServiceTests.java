package com.jb.ElvinaFinalSpringProject;

import com.jb.ElvinaFinalSpringProject.Beans.Company;
import com.jb.ElvinaFinalSpringProject.Beans.Customer;
import com.jb.ElvinaFinalSpringProject.Beans.LoginCredentials;
import com.jb.ElvinaFinalSpringProject.Beans.TokenRecord;
import com.jb.ElvinaFinalSpringProject.Repositories.CompanyRepository;
import com.jb.ElvinaFinalSpringProject.Repositories.CustomerRepository;
import com.jb.ElvinaFinalSpringProject.controller.AdminController;
import lombok.extern.slf4j.Slf4j;
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
class AdminServiceTests {
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final AdminController adminController;

    // Common data must be static
    private static TokenRecord tokenRecord;
    private static final List<Company> companyTemplates = new ArrayList<>();
    private static final List<Customer> customerTemplates = new ArrayList<>();

    @Autowired
    public AdminServiceTests(CustomerRepository customerRepository, CompanyRepository companyRepository, AdminController adminController) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.adminController = adminController;
    }

    @BeforeAll
    void initTests() {
        companyTemplates.add(Company.builder().name("Company1").email("company1@company1.com").password("company1").build());
        companyTemplates.add(Company.builder().name("Company2").email("company2@company2.com").password("company2").build());
        companyTemplates.add(Company.builder().name("Company3").email("company3@company3.com").password("company3").build());
        companyTemplates.add(Company.builder().name("Company4").email("company4@company4.com").password("company4").build());
        customerTemplates.add(Customer.builder().firstName("Customer1Name").lastName("Customer1LastName").email("customer1@customer1.com").password("customer1").build());
        customerTemplates.add(Customer.builder().firstName("Customer2Name").lastName("Customer2LastName").email("customer2@customer2.com").password("customer2").build());
        customerTemplates.add(Customer.builder().firstName("Customer3Name").lastName("Customer3LastName").email("customer3@customer3.com").password("customer3").build());
    }

    @AfterAll
    void clearAfterTests() {
        companyRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    @Order(1)
    void loginTest() {
        LoginCredentials credentials = LoginCredentials.builder().email("admin@admin.com").password("admin").build();
        ResponseEntity<?> response = adminController.login(credentials);
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        tokenRecord = (TokenRecord) response.getBody();
        Assert.notNull(tokenRecord, "Admin token record returned null");
        Assert.notNull(tokenRecord.getToken(), "Token record has no token");
    }

    @Test
    @Order(2)
    void addCompanyTest() {
        for (Company company : companyTemplates) {
            adminController.addCompany(tokenRecord.getToken(), company);
            log.info("Created company {}", company);
        }
    }

    @Test
    @Order(3)
    void getCompanyOneTest() {
        int id = companyTemplates.get(0).getId();
        log.info("Testing get company with company id - {}", id);
        ResponseEntity<?> response = adminController.getOneCompany(tokenRecord.getToken(), id);
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.FOUND), "Status returned not as expected");
        Company company = (Company) response.getBody();
        log.info("The following company returned {}", company);
        Assert.notNull(company, "Company returned as null");
        Assert.isTrue(id == company.getId(), "The id returned is not correct");
    }

    @Test
    @Order(4)
    void deleteCompanyTest() {
        int id = companyTemplates.get(0).getId();
        log.info("Testing delete company with company id - {}", id);
        ResponseEntity<?> deleteResponse = adminController.deleteCompany(tokenRecord.getToken(), id);
        Assert.isTrue(deleteResponse.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        ResponseEntity<?> getResponse = adminController.getOneCompany(tokenRecord.getToken(), id);
        Assert.isTrue(getResponse.getStatusCode().equals(HttpStatus.NOT_FOUND), "Status returned not as expected");
    }

    @Test
    @Order(5)
    void updateCompanyTest() {
        Company company = companyTemplates.get(1);
        String newEmail = "updated@updated.com";
        log.info("Company before update {}", company);
        company.setEmail(newEmail);
        ResponseEntity<?> updateResponse = adminController.updateCompany(tokenRecord.getToken(), company);
        Assert.isTrue(updateResponse.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        ResponseEntity<?> getResponse = adminController.getOneCompany(tokenRecord.getToken(), company.getId());
        Assert.isTrue(getResponse.getStatusCode().equals(HttpStatus.FOUND), "Status returned not as expected");
        Company companyAfterUpdate = (Company) getResponse.getBody();
        log.info("Company after update {}", companyAfterUpdate);
        Assert.notNull(companyAfterUpdate, "company returned as null");
        Assert.notNull(companyAfterUpdate.getEmail(), "company email returned as null");
        Assert.isTrue(newEmail.equals(companyAfterUpdate.getEmail()), "The email was not updated");
    }

    @Test
    @Order(6)
    void getAllCompaniesTest() {
        ResponseEntity<?> response = adminController.getAllCompanies(tokenRecord.getToken());
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        List<Company> companies = (List<Company>) response.getBody();
        Assert.notNull(companies, "Companies list returned as null");
        Assert.isTrue(companies.size() == 3, "Not all companies returned");
    }

    @Test
    @Order(7)
    void addCustomerTest() {
        for (Customer customer : customerTemplates) {
            ResponseEntity<?> response = adminController.addCustomer(tokenRecord.getToken(), customer);
            Assert.isTrue(response.getStatusCode().equals(HttpStatus.CREATED), "Status returned not as expected");
            log.info("Created customer {}", customer);
        }
    }

    @Test
    @Order(8)
    void getCustomerOneTest() {
        int id = customerTemplates.get(0).getId();
        log.info("Testing get customer with id - {}", id);
        ResponseEntity<?> response = adminController.getOneCustomer(tokenRecord.getToken(), id);
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.FOUND), "Status returned not as expected");
        Customer customer = (Customer) response.getBody();
        Assert.notNull(customer, "Customer returned as null");
        Assert.isTrue(id == customer.getId(), "The id returned is not correct");
    }

    @Test
    @Order(9)
    void deleteCustomerTest() {
        int id = customerTemplates.get(0).getId();
        log.info("Testing delete customer with id - {}", id);
        ResponseEntity<?> response = adminController.deleteCustomer(tokenRecord.getToken(), id);
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        ResponseEntity<?> responseAfterDelete = adminController.getOneCustomer(tokenRecord.getToken(), id);
        Assert.isTrue(responseAfterDelete.getStatusCode().equals(HttpStatus.NOT_FOUND), "Status returned not as expected");
    }

    @Test
    @Order(10)
    void updateCustomerTest() {
        Customer customer = customerTemplates.get(1);
        String newPassword = "newPassword";
        customer.setPassword(newPassword);
        ResponseEntity<?> updateResponse = adminController.updateCustomer(tokenRecord.getToken(), customer);
        Assert.isTrue(updateResponse.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        ResponseEntity<?> getResponse = adminController.getOneCustomer(tokenRecord.getToken(), customer.getId());
        Assert.isTrue(getResponse.getStatusCode().equals(HttpStatus.FOUND), "Status returned not as expected");
        Customer customerAfterUpdate = (Customer) getResponse.getBody();
        Assert.isTrue(newPassword.equals(customerAfterUpdate.getPassword()), "The update of password failed");
    }

    @Test
    @Order(11)
    void getAllCustomersTest() {
        ResponseEntity<?> response = adminController.getAllCustomers(tokenRecord.getToken());
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
        List<Customer> customers = (List<Customer>) response.getBody();
        Assert.notNull(customers, "Customers list returned as null");
        Assert.isTrue(customers.size() == 2, "Not all customers returned");
    }

    @Test
    @Order(12)
    void startCleaningJob() {
        ResponseEntity<?> response = adminController.startCleaningJob(tokenRecord.getToken());
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
    }

    @Test
    @Order(13)
    void stopCleaningJob() {
        ResponseEntity<?> response = adminController.stopCleaningJob(tokenRecord.getToken());
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
    }

    @Test
    @Order(14)
    void logOut() {
        ResponseEntity<?> response = adminController.logout(tokenRecord.getToken());
        Assert.isTrue(response.getStatusCode().equals(HttpStatus.OK), "Status returned not as expected");
    }
}
