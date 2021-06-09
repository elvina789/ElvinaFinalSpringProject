package com.jb.ElvinaFinalSpringProject.Exeptions;

public class CustomerServiceException extends RuntimeException {
    public CustomerServiceException(String message) {
        super(message);
    }

    public CustomerServiceException(Throwable cause) {
        super(cause);
    }
}