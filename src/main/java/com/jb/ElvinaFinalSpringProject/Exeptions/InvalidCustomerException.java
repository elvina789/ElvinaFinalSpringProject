package com.jb.ElvinaFinalSpringProject.Exeptions;

public class InvalidCustomerException extends RuntimeException {
    public InvalidCustomerException() {
        super("The customer is invalid");
    }

    public InvalidCustomerException(String message) {
        super(message);
    }
}
