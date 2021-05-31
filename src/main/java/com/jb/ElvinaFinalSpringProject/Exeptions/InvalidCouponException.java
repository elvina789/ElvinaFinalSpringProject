package com.jb.ElvinaFinalSpringProject.Exeptions;

public class InvalidCouponException extends Exception {
    public InvalidCouponException() {
        super("The coupon is invalid");
    }
}
