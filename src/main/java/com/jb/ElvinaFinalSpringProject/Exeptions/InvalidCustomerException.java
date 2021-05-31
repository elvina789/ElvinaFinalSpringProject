package com.jb.ElvinaFinalSpringProject.Exeptions;

public class InvalidCustomerException extends Exception {
    public InvalidCustomerException() {
        super("The customer is invalid");
    }
}
