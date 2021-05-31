package com.jb.ElvinaFinalSpringProject.Exeptions;

public class InvalidCompanyException extends Exception {
    public InvalidCompanyException() {
        super("The company is invalid");
    }
}
