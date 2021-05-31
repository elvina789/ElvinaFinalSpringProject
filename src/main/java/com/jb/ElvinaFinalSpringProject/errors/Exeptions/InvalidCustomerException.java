package com.jb.ElvinaFinalSpringProject.errors.Exeptions;

public class InvalidCustomerException extends Exception {
    public InvalidCustomerException() {
        super("The customer is invalid");
    }
}
