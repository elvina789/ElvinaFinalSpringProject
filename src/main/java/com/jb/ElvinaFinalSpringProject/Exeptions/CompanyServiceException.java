package com.jb.ElvinaFinalSpringProject.Exeptions;

public class CompanyServiceException extends RuntimeException {
    public CompanyServiceException(String message) {
        super(message);
    }

    public CompanyServiceException(Throwable cause) {
        super(cause);
    }
}