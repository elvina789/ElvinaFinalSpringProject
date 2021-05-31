package com.jb.ElvinaFinalSpringProject.Exeptions;

public class CustomerServiceException extends Exception {
    public CustomerServiceException(String message) {
        super(message);
    }

    public CustomerServiceException(Throwable cause) {
        super(cause);
    }
}