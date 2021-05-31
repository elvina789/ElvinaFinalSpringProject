package com.jb.ElvinaFinalSpringProject.errors.handlers;

import com.jb.ElvinaFinalSpringProject.errors.Exeptions.CompanyServiceException;
import com.jb.ElvinaFinalSpringProject.errors.Exeptions.LoginManagerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler(CompanyServiceException.class)
    public ResponseEntity<?> handleCompanyServiceException(HttpServletResponse response, CompanyServiceException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(LoginManagerException.class)
    public ResponseEntity<?> handleCompanyServiceException(HttpServletResponse response, LoginManagerException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
