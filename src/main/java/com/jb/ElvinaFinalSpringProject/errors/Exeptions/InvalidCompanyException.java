package com.jb.ElvinaFinalSpringProject.errors.Exeptions;

public class InvalidCompanyException extends Exception {
    public InvalidCompanyException() {
        super("The company is invalid");
    }
}
