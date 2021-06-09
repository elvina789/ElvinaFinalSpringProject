package com.jb.ElvinaFinalSpringProject.Exeptions;

public class AdminServiceException extends RuntimeException {
    public AdminServiceException(String message) {
        super(message);
    }

    public AdminServiceException(Throwable cause) {
        super(cause);
    }
}
