package com.jb.ElvinaFinalSpringProject.services;

import com.jb.ElvinaFinalSpringProject.Beans.Company;
import com.jb.ElvinaFinalSpringProject.Beans.Customer;
import com.jb.ElvinaFinalSpringProject.Beans.Enums.ClientType;
import com.jb.ElvinaFinalSpringProject.Beans.Session;
import com.jb.ElvinaFinalSpringProject.Exeptions.AdminServiceException;
import com.jb.ElvinaFinalSpringProject.Exeptions.InvalidCompanyException;
import com.jb.ElvinaFinalSpringProject.Exeptions.InvalidCustomerException;
import com.jb.ElvinaFinalSpringProject.Repositories.CompanyRepository;
import com.jb.ElvinaFinalSpringProject.Repositories.CouponRepository;
import com.jb.ElvinaFinalSpringProject.Repositories.CustomerRepository;
import com.jb.ElvinaFinalSpringProject.security.SessionManager;
import com.jb.ElvinaFinalSpringProject.services.interfaces.AdminService;
import com.jb.ElvinaFinalSpringProject.utils.Constants;
import com.jb.ElvinaFinalSpringProject.validation.BeanValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation for the AdminService, that contains the logic of Admin Service functions
 */

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final BeanValidator beanValidator;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;
    private final SessionManager sessionManager;

    /**
     * Constructor of the AdminServiceImpl type object
     *
     * @param beanValidator      beanValidator of AdminServiceImpl object
     * @param companyRepository  companyRepository of AdminServiceImpl object
     * @param customerRepository customerRepository of AdminServiceImpl object
     * @param couponRepository   couponRepository of AdminServiceImpl object
     * @param sessionManager     sessionManager of AdminServiceImpl object
     */
    @Autowired
    public AdminServiceImpl(BeanValidator beanValidator, CompanyRepository companyRepository, CustomerRepository customerRepository, CouponRepository couponRepository, SessionManager sessionManager) {
        this.beanValidator = beanValidator;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.couponRepository = couponRepository;
        this.sessionManager = sessionManager;
    }

    /**
     * Method to login Admin Service
     *
     * @param email    email for login
     * @param password password for login
     * @return returns session if succseeded to login and null if not
     */
    @Override
    public Session login(String email, String password) {
//        email = new String(Base64.getDecoder().decode(email));
//        password = new String(Base64.getDecoder().decode(password));
        if (Constants.ADMIN_EMAIL.equals(email) && Constants.ADMIN_PASSWORD.equals(password)) {
            return sessionManager.createSession(Constants.ADMIN_USER_ID, ClientType.Administrator);
        } else {
            return null;
        }
    }

    /**
     * Method to logout Admin Service
     *
     * @param token token for logout
     */
    @Override
    public void logout(String token) {
        sessionManager.deleteSession(token);
    }

    /**
     * Method to add Company
     *
     * @param company company to add
     * @throws InvalidCompanyException exception
     * @throws AdminServiceException   exception
     */
    @Override
    public void addCompany(Company company) throws InvalidCompanyException, AdminServiceException {
        log.info("Calling addCompany()");
        try {
            beanValidator.validate(company);
            if (!companyRepository.existsCompanyByNameOrEmail(company.getName(), company.getEmail())) {
                log.info("Writing company with id {} to database", company.getId());
                companyRepository.save(company);
                log.info("Finished writing company with id {} to database", company.getId());
            } else {
                throw new AdminServiceException("Company with name - " + company.getName() + " or email - " + company.getEmail() + " already exists");
            }
        } catch (RuntimeException e) {
            throw new AdminServiceException("Something gone wrong when we tried to add company " + e.getMessage());
        }
    }

    /**
     * Method to update Company
     *
     * @param company company to update
     * @throws AdminServiceException   exception
     * @throws InvalidCompanyException exception
     */
    @Override
    public void updateCompany(Company company) throws AdminServiceException, InvalidCompanyException {
        log.info("Calling updateCompany()");
        try {
            beanValidator.validate(company);
            if (!companyRepository.existsById(company.getId())) {
                throw new AdminServiceException("Company with id - " + company.getId() + " does not exists");
            }
            Company companyInDb = companyRepository.getOne(company.getId());
            if (companyInDb.getName().equals(company.getName())) {
                companyRepository.save(company); // if same name perform update
            } else {
                throw new AdminServiceException("Updating company name is forbidden");
            }
        } catch (RuntimeException e) {
            throw new AdminServiceException("Something gone wrong when we tried to update company " + e.getMessage());
        }
    }

    /**
     * Method to delete Company
     *
     * @param companyId id of the company that we want to delete
     * @throws AdminServiceException exeption
     */
    @Override
    public void deleteCompany(int companyId) throws AdminServiceException {
        try {
            if (companyRepository.existsById(companyId)) {
                companyRepository.deleteById(companyId);
            } else {
                throw new AdminServiceException("Company with id - " + companyId + " does not exist");
            }
        } catch (RuntimeException e) {
            throw new AdminServiceException("Something gone wrong when we tried to delete company " + e.getMessage());
        }
    }

    /**
     * Method that gets all companies
     *
     * @return List of companies
     * @throws AdminServiceException exception
     */
    @Override
    public List<Company> getAllCompanies() throws AdminServiceException {
        try {
            return companyRepository.findAll();
        } catch (RuntimeException e) {
            throw new AdminServiceException("Something gone wrong when we tried to get all customers " + e.getMessage());
        }
    }

    /**
     * Method to get one company
     *
     * @param companyId id to get company
     * @return company
     * @throws AdminServiceException exception
     */

    @Override
    public Company getOneCompany(int companyId) throws AdminServiceException {
        try {
            if (companyRepository.existsById(companyId)) {
                return companyRepository.getOne(companyId);
            } else {
                return null;
            }
        } catch (RuntimeException e) {
            throw new AdminServiceException("Something gone wrong when we tried to get customer by id " + e.getMessage());
        }
    }

    /**
     * Method that adds customer
     *
     * @param customer customer that we want to edd
     * @throws AdminServiceException    exception
     * @throws InvalidCustomerException exception
     */
    @Override
    public void addCustomer(Customer customer) throws AdminServiceException, InvalidCustomerException {
        log.info("Calling addCustomer()");
        try {
            beanValidator.validate(customer);
            if (!customerRepository.existsCustomerByEmail(customer.getEmail())) {
                customerRepository.save(customer);
            } else {
                throw new AdminServiceException("Customer with email - " + customer.getEmail() + " already exists");
            }
        } catch (RuntimeException e) {
            throw new AdminServiceException("Something gone wrong when we tried to add customer " + e.getMessage());
        }
    }

    /**
     * Method to update Customer
     *
     * @param customer customer that we want to update
     * @throws AdminServiceException    exception
     * @throws InvalidCustomerException exception
     */

    @Override
    public void updateCustomer(Customer customer) throws AdminServiceException, InvalidCustomerException {
        log.info("Calling updateCustomer()");
        try {
            beanValidator.validate(customer);
            if (customerRepository.existsById(customer.getId())) {
                customerRepository.save(customer);
            }
        } catch (RuntimeException e) {
            throw new AdminServiceException("Something gone wrong when we tried to update  customer " + e.getMessage());
        }
    }

    /**
     * Method to delete customer by its id
     */
    @Override
    public void deleteCustomer(int customerId) throws AdminServiceException {
        try {
            if (customerRepository.existsById(customerId)) {
                customerRepository.deleteById(customerId);
            } else {
                throw new AdminServiceException("There is no customer with id - " + customerId);
            }
        } catch (RuntimeException e) {
            throw new AdminServiceException("Something gone wrong when we tried to delete customer " + e.getMessage());
        }
    }

    /**
     * Method to get all customers
     *
     * @return list of customers
     * @throws AdminServiceException exception
     */
    @Override
    public List<Customer> getAllCustomers() throws AdminServiceException {
        try {
            return customerRepository.findAll();
        } catch (RuntimeException e) {
            throw new AdminServiceException("Something gone wrong when we tried to get all customers " + e.getMessage());
        }
    }

    /**
     * Method to get one customer by its id
     *
     * @param customerId customer id
     * @return customer
     * @throws AdminServiceException exception
     */
    @Override
    public Customer getOneCustomer(int customerId) throws AdminServiceException {
        try {
            if (customerRepository.existsById(customerId)) {
                return customerRepository.getOne(customerId);
            } else {
                return null;
            }
        } catch (RuntimeException e) {
            throw new AdminServiceException("Something gone wrong when we tried to get customer " + e.getMessage());
        }
    }
}
