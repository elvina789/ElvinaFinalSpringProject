package com.jb.ElvinaFinalSpringProject.Exeptions;

public class InvalidCompanyException extends RuntimeException {
    public InvalidCompanyException() {
        super("The company is invalid");
    }

    public InvalidCompanyException(String message) {
        super(message);
    }
}
