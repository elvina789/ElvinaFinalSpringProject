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

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final BeanValidator beanValidator;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final CouponRepository couponRepository;
    private final SessionManager sessionManager;

    @Autowired
    public AdminServiceImpl(BeanValidator beanValidator, CompanyRepository companyRepository, CustomerRepository customerRepository, CouponRepository couponRepository, SessionManager sessionManager) {
        this.beanValidator = beanValidator;
        this.companyRepository = companyRepository;
        this.customerRepository = customerRepository;
        this.couponRepository = couponRepository;
        this.sessionManager = sessionManager;
    }

    @Override
    public Session login(String email, String password) {
        if (Constants.ADMIN_EMAIL.equals(email) && Constants.ADMIN_PASSWORD.equals(password)) {
            return sessionManager.createSession(Constants.ADMIN_USER_ID, ClientType.Administrator);
        } else {
            return null;
        }
    }

    @Override
    public void logout(String token) {
        sessionManager.deleteSession(token);
    }

    @Override
    public void addCompany(Company company) throws InvalidCompanyException, AdminServiceException {
        log.info("Calling addCompany()");
        try {
            if (!beanValidator.validate(company)) {
                throw new InvalidCompanyException();
            }
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

    @Override
    public void updateCompany(Company company) throws AdminServiceException, InvalidCompanyException {
        log.info("Calling updateCompany()");
        try {
            if (!beanValidator.validate(company)) {
                throw new InvalidCompanyException();
            }
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

    @Override
    public List<Company> getAllCompanies() throws AdminServiceException {
        try {
            return companyRepository.findAll();
        } catch (RuntimeException e) {
            throw new AdminServiceException("Something gone wrong when we tried to get all customers " + e.getMessage());
        }
    }

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

    @Override
    public void addCustomer(Customer customer) throws AdminServiceException, InvalidCustomerException {
        log.info("Calling addCustomer()");
        try {
            if (!beanValidator.validate(customer)) {
                throw new InvalidCustomerException();
            }
            if (!customerRepository.existsCustomerByEmail(customer.getEmail())) {
                customerRepository.save(customer);
            } else {
                throw new AdminServiceException("Customer with email - " + customer.getEmail() + " already exists");
            }
        } catch (RuntimeException e) {
            throw new AdminServiceException("Something gone wrong when we tried to add customer " + e.getMessage());
        }
    }


    @Override
    public void updateCustomer(Customer customer) throws AdminServiceException, InvalidCustomerException {
        log.info("Calling updateCustomer()");
        try {
            if (!beanValidator.validate(customer)) {
                throw new InvalidCustomerException();
            }

            if (customerRepository.existsById(customer.getId())) {
                customerRepository.save(customer);
            }
        } catch (RuntimeException e) {
            throw new AdminServiceException("Something gone wrong when we tried to update  customer " + e.getMessage());
        }
    }

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

    @Override
    public List<Customer> getAllCustomers() throws AdminServiceException {
        try {
            return customerRepository.findAll();
        } catch (RuntimeException e) {
            throw new AdminServiceException("Something gone wrong when we tried to get all customers " + e.getMessage());
        }
    }

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
