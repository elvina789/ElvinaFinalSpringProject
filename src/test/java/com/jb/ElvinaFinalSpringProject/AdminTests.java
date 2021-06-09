package com.jb.ElvinaFinalSpringProject;

import com.jb.ElvinaFinalSpringProject.Beans.Company;
import com.jb.ElvinaFinalSpringProject.Beans.Customer;
import com.jb.ElvinaFinalSpringProject.Beans.LoginCredentials;
import com.jb.ElvinaFinalSpringProject.Beans.Session;
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
class AdminTests {
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final AdminController adminController;

    // Common data must be static
    private static Session session;
    private static final List<Company> companyTemplates = new ArrayList<>();
    private static final List<Customer> customerTemplates = new ArrayList<>();

    @Autowired
    public AdminTests(CustomerRepository customerRepository, CompanyRepository companyRepository, AdminController adminController) {
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
        HttpStatus expected = HttpStatus.OK;
        log.info("Expected:");
        log.info("Expected response status {}", expected);
        log.info("Expected response body not empty");
        log.info("Actual:");
        ResponseEntity<?> response = adminController.login(credentials);
        log.info("Actual response status {}", response.getStatusCode());
        log.info("Actual response body {}", response.getBody());
        Assert.isTrue(response.getStatusCode().equals(expected), "Status returned not as expected");
        session = (Session) response.getBody();
        Assert.notNull(session, "Admin token record returned null");
        Assert.notNull(session.getToken(), "Token record has no token");
    }

    @Test
    @Order(1)
    void loginTestIncorrectCredentials() {
        HttpStatus expectedStatus = HttpStatus.OK;
        String expectedBody = "Incorrect email or password";
        log.info("Expected:");
        log.info("Expected response status {}", expectedStatus);
        log.info("Expected response body {}", expectedBody);
        log.info("Actual:");
        LoginCredentials credentials = LoginCredentials.builder().email("admin2@admin.com").password("admin2").build();
        ResponseEntity<?> response = adminController.login(credentials);
        log.info("Actual response status {}", response.getStatusCode());
        log.info("Actual response body {}", response.getBody());
        Assert.isTrue(response.getStatusCode().equals(expectedStatus), "Status returned not as expectedStatus");
        Assert.isTrue(expectedBody.equals(response.getBody()), "Body returned not as expected");
    }

    @Test
    @Order(2)
    void addCompanyTest() {
        for (Company company : companyTemplates) {
            HttpStatus expectedStatus = HttpStatus.CREATED;
            log.info("Expected:");
            log.info("Expected response status {}", expectedStatus);
            log.info("Expected response body {}", company);
            log.info("Actual:");
            ResponseEntity<Company> response = adminController.addCompany(session.getToken(), company);
            log.info("Actual response status {}", response.getStatusCode());
            log.info("Actual response body {}", response.getBody());
            Assert.isTrue(response.getStatusCode().equals(expectedStatus), "Company " + company.getId() + " not created");
        }
    }

    @Test
    @Order(3)
    void getCompanyOneTest() {
        HttpStatus expectedStatus = HttpStatus.FOUND;
        Company expectedCompany = companyTemplates.get(0);
        log.info("Expected:");
        log.info("Expected response status {}", expectedStatus);
        log.info("Expected response body {}", expectedCompany);
        log.info("Actual:");
        ResponseEntity<Company> response = adminController.getOneCompany(session.getToken(), expectedCompany.getId());
        log.info("Actual response status {}", response.getStatusCode());
        log.info("Actual response body {}", response.getBody());
        Assert.isTrue(response.getStatusCode().equals(expectedStatus), "Status returned not as expected");
        Company responseCompany = response.getBody();
        Assert.notNull(responseCompany, "Company returned as null");
        Assert.isTrue(expectedCompany.getId() == responseCompany.getId(), "The id returned is not correct");
    }

    @Test
    @Order(4)
    void deleteCompanyTest() {
        HttpStatus expectedStatusForDelete = HttpStatus.OK;
        Company expectedCompany = companyTemplates.get(0);
        log.info("Expected for delete:");
        log.info("Expected response status {}", expectedStatusForDelete);
        log.info("Expected response body {}", expectedCompany);
        log.info("Actual:");
        log.info("Testing delete company with company id - {}", expectedCompany.getId());
        ResponseEntity<?> deleteResponse = adminController.deleteCompany(session.getToken(), expectedCompany.getId());
        log.info("Actual response status {}", deleteResponse.getStatusCode());
        log.info("Actual response body {}", deleteResponse.getBody());
        Assert.isTrue(deleteResponse.getStatusCode().equals(expectedStatusForDelete), "Status returned not as expected");

        HttpStatus expectedStatusForGet = HttpStatus.NOT_FOUND;
        log.info("Expected for get:");
        log.info("Expected response status {}", expectedStatusForDelete);
        ResponseEntity<?> getResponse = adminController.getOneCompany(session.getToken(), expectedCompany.getId());
        log.info("Actual response status {}", getResponse.getStatusCode());
        Assert.isTrue(getResponse.getStatusCode().equals(HttpStatus.NOT_FOUND), "Status returned not as expected");
    }

    @Test
    @Order(5)
    void updateCompanyTest() {
        Company company = companyTemplates.get(1);
        log.info("Company before update {}", company);
        String newEmail = "updated@updated.com";
        company.setEmail(newEmail);
        HttpStatus expectedStatus = HttpStatus.OK;
        log.info("Expected:");
        log.info("Expected response status {}", expectedStatus);
        log.info("Expected response body {}", company);
        log.info("Actual:");
        ResponseEntity<?> updateResponse = adminController.updateCompany(session.getToken(), company);
        log.info("Actual response status {}", updateResponse.getStatusCode());
        log.info("Actual response body {}", updateResponse.getBody());
        Assert.isTrue(updateResponse.getStatusCode().equals(expectedStatus), "Status returned not as expected");
        ResponseEntity<?> getResponse = adminController.getOneCompany(session.getToken(), company.getId());
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
        HttpStatus expectedStatus = HttpStatus.OK;
        int expectedSize = 3;
        log.info("Expected:");
        log.info("Expected response status {}", expectedStatus);
        log.info("Expected size {}", expectedStatus);
        ResponseEntity<?> response = adminController.getAllCompanies(session.getToken());
        log.info("Actual response status {}", response.getStatusCode());
        Assert.isTrue(response.getStatusCode().equals(expectedStatus), "Status returned not as expected");
        List<Company> companies = (List<Company>) response.getBody();
        Assert.notNull(companies, "Companies list returned as null");
        log.info("Actual size {}", companies.size());
        Assert.isTrue(companies.size() == expectedSize, "Not all companies returned");
    }

    @Test
    @Order(7)
    void addCustomerTest() {
        for (Customer customer : customerTemplates) {
            HttpStatus expectedStatus = HttpStatus.CREATED;
            log.info("Expected:");
            log.info("Expected response status {}", expectedStatus);
            ResponseEntity<Customer> response = adminController.addCustomer(session.getToken(), customer);
            log.info("Actual response status {}", response.getStatusCode());
            Assert.isTrue(response.getStatusCode().equals(expectedStatus), "Status returned not as expected");
            log.info("Created customer {}", customer);
        }
    }

    @Test
    @Order(8)
    void getCustomerOneTest() {
        HttpStatus expectedStatus = HttpStatus.FOUND;
        Customer expectedCustomer = customerTemplates.get(0);
        log.info("Expected:");
        log.info("Expected response status {}", expectedStatus);
        log.info("Expected customer {}", expectedCustomer);
        ResponseEntity<Customer> response = adminController.getOneCustomer(session.getToken(), expectedCustomer.getId());
        log.info("Actual response status {}", response.getStatusCode());
        log.info("Actual response customer {}", response.getBody());
        Assert.isTrue(response.getStatusCode().equals(expectedStatus), "Status returned not as expected");
        Customer customer = response.getBody();
        Assert.notNull(customer, "Customer returned as null");
        Assert.isTrue(expectedCustomer.getId() == customer.getId(), "The id returned is not correct");
    }

    @Test
    @Order(9)
    void deleteCustomerTest() {
        HttpStatus expectedStatus = HttpStatus.OK;
        log.info("Expected:");
        log.info("Expected response status {}", expectedStatus);
        int id = customerTemplates.get(0).getId();
        log.info("Testing delete customer with id - {}", id);
        ResponseEntity<?> response = adminController.deleteCustomer(session.getToken(), id);
        log.info("Actual response status {}", response.getStatusCode());
        Assert.isTrue(response.getStatusCode().equals(expectedStatus), "Status returned not as expected");
        ResponseEntity<?> responseAfterDelete = adminController.getOneCustomer(session.getToken(), id);
        Assert.isTrue(responseAfterDelete.getStatusCode().equals(HttpStatus.NOT_FOUND), "Status returned not as expected");
        log.info("Customer {} not found after delete", id);
    }

    @Test
    @Order(10)
    void updateCustomerTest() {
        HttpStatus expectedStatusForUpdate = HttpStatus.OK;
        Customer customer = customerTemplates.get(1);
        log.info("Customer before update {}", customer);
        String newPassword = "newPassword";
        customer.setPassword(newPassword);
        log.info("Expected:");
        log.info("Expected response status for update {}", expectedStatusForUpdate);
        log.info("Expected customer after update {}", customer);
        ResponseEntity<Customer> updateResponse = adminController.updateCustomer(session.getToken(), customer);
        log.info("Actual:");
        log.info("Actual response status for update {}", updateResponse.getStatusCode());
        log.info("Actual customer after update {}", updateResponse.getBody());
        Assert.isTrue(updateResponse.getStatusCode().equals(expectedStatusForUpdate), "Status returned not as expected");
        ResponseEntity<?> getResponse = adminController.getOneCustomer(session.getToken(), customer.getId());
        Assert.isTrue(getResponse.getStatusCode().equals(HttpStatus.FOUND), "Status returned not as expected");
        Customer customerAfterUpdate = (Customer) getResponse.getBody();
        Assert.isTrue(newPassword.equals(customerAfterUpdate.getPassword()), "The update of password failed");
        log.info("Customer was updated");
    }

    @Test
    @Order(11)
    void getAllCustomersTest() {
        HttpStatus expectedResponse = HttpStatus.OK;
        int expectedSize = 2;
        log.info("Expected:");
        log.info("Expected response status {}", expectedResponse);
        log.info("Expected size {}", expectedSize);
        ResponseEntity<List<Customer>> response = adminController.getAllCustomers(session.getToken());
        log.info("Actual:");
        log.info("Actual response status  {}", response.getStatusCode());
        Assert.isTrue(response.getStatusCode().equals(expectedResponse), "Status returned not as expected");
        List<Customer> customers = response.getBody();
        Assert.notNull(customers, "Customers list returned as null");
        log.info("Actual size {}", customers.size());
        Assert.isTrue(customers.size() == expectedSize, "Not all customers returned");
    }

    @Test
    @Order(12)
    void startCleaningJob() {
        HttpStatus expectedStatus = HttpStatus.OK;
        log.info("Expected:");
        log.info("Expected response status {}", expectedStatus);
        ResponseEntity<?> response = adminController.startCleaningJob(session.getToken());
        log.info("Actual response status  {}", response.getStatusCode());
        Assert.isTrue(response.getStatusCode().equals(expectedStatus), "Status returned not as expected");
    }

    @Test
    @Order(13)
    void stopCleaningJob() {
        HttpStatus expectedStatus = HttpStatus.OK;
        log.info("Expected:");
        log.info("Expected response status {}", expectedStatus);
        ResponseEntity<?> response = adminController.stopCleaningJob(session.getToken());
        log.info("Actual response status  {}", response.getStatusCode());
        Assert.isTrue(response.getStatusCode().equals(expectedStatus), "Status returned not as expected");
    }

    @Test
    @Order(14)
    void logOut() {
        HttpStatus expectedStatus = HttpStatus.OK;
        log.info("Expected:");
        log.info("Expected response status {}", expectedStatus);
        ResponseEntity<?> response = adminController.logout(session.getToken());
        log.info("Actual response status  {}", response.getStatusCode());
        Assert.isTrue(response.getStatusCode().equals(expectedStatus), "Status returned not as expected");
    }
}
