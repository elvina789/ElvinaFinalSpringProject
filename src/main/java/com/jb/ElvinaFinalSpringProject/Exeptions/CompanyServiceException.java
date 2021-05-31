package com.jb.ElvinaFinalSpringProject.Exeptions;

public class CompanyServiceException extends Exception {
    public CompanyServiceException(String message) {
        super(message);
    }

    public CompanyServiceException(Throwable cause) {
        super(cause);
    }
}