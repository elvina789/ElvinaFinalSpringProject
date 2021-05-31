package com.jb.ElvinaFinalSpringProject.services.interfaces;

import com.jb.ElvinaFinalSpringProject.Beans.Company;
import com.jb.ElvinaFinalSpringProject.Beans.Customer;
import com.jb.ElvinaFinalSpringProject.Beans.Session;
import com.jb.ElvinaFinalSpringProject.Exeptions.AdminServiceException;
import com.jb.ElvinaFinalSpringProject.Exeptions.InvalidCompanyException;
import com.jb.ElvinaFinalSpringProject.Exeptions.InvalidCustomerException;

import java.util.List;

public interface AdminService {
    Session login(String email, String password);

    void logout(String token);

    void addCompany(Company company) throws InvalidCompanyException, AdminServiceException;

    void updateCompany(Company company) throws AdminServiceException, InvalidCompanyException;

    void deleteCompany(int companyId) throws AdminServiceException;

    List<Company> getAllCompanies() throws AdminServiceException;

    Company getOneCompany(int companyId) throws AdminServiceException;

    void addCustomer(Customer customer) throws AdminServiceException, InvalidCustomerException;

    void updateCustomer(Customer customer) throws AdminServiceException, InvalidCustomerException;

    void deleteCustomer(int customerId) throws AdminServiceException;

    List<Customer> getAllCustomers() throws AdminServiceException;

    Customer getOneCustomer(int customerId) throws AdminServiceException;
}
