package com.jb.ElvinaFinalSpringProject.services.interfaces;

import com.jb.ElvinaFinalSpringProject.Beans.Company;
import com.jb.ElvinaFinalSpringProject.Beans.Customer;
import com.jb.ElvinaFinalSpringProject.Beans.Session;
import com.jb.ElvinaFinalSpringProject.Exeptions.AdminServiceException;
import com.jb.ElvinaFinalSpringProject.Exeptions.InvalidCompanyException;
import com.jb.ElvinaFinalSpringProject.Exeptions.InvalidCustomerException;

import java.util.List;

/**
 * Interface that contains functions for Admin Service
 */

public interface AdminService {
    /**
     * Method to login Admin Service
     * @param email    email for login
     * @param password password for login
     * @return returns session if succseeded to login and null if not
     */
    Session login(String email, String password);

    /**
     * Method to logout Admin Service
     * @param token token for logout
     */
    void logout(String token);

    /**
     * Method to add Company
     *
     * @param company company to add
     * @throws InvalidCompanyException exception
     * @throws AdminServiceException   exception
     */
    void addCompany(Company company) throws InvalidCompanyException, AdminServiceException;

    /**
     * Method to update Company
     * @param company company to update
     * @throws AdminServiceException   exception
     * @throws InvalidCompanyException exception
     */
    void updateCompany(Company company) throws AdminServiceException, InvalidCompanyException;

    /**
     * Method to delete Company
     * @param companyId id of the company that we want to delete
     * @throws AdminServiceException exeption
     */
    void deleteCompany(int companyId) throws AdminServiceException;

    /**
     * Method that gets all companies
     * @return List of companies
     * @throws AdminServiceException exception
     */
    List<Company> getAllCompanies() throws AdminServiceException;

    /**
     * Method to get one company
     * @param companyId id to get company
     * @return company
     * @throws AdminServiceException exception
     */
    Company getOneCompany(int companyId) throws AdminServiceException;

    /**
     * Method that adds customer
     * @param customer customer that we want to edd
     * @throws AdminServiceException    exception
     * @throws InvalidCustomerException exception
     */
    void addCustomer(Customer customer) throws AdminServiceException, InvalidCustomerException;

    /**
     * Method to update Customer
     * @param customer customer that we want to update
     * @throws AdminServiceException    exception
     * @throws InvalidCustomerException exception
     */
    void updateCustomer(Customer customer) throws AdminServiceException, InvalidCustomerException;

    /**
     *Method to delete customer by its id
     */
    void deleteCustomer(int customerId) throws AdminServiceException;

    /**
     * Method to get all customers
     * @return list of customers
     * @throws AdminServiceException exception
     */
    List<Customer> getAllCustomers() throws AdminServiceException;

    /**
     * Method to get one customer by its id
     * @param customerId customer id
     * @return customer
     * @throws AdminServiceException exception
     */
    Customer getOneCustomer(int customerId) throws AdminServiceException;
}
