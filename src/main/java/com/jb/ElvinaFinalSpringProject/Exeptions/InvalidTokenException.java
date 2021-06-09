package com.jb.ElvinaFinalSpringProject.Exeptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super("The token provided is invalid");
    }
}
