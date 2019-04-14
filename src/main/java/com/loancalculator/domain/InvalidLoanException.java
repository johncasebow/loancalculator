package com.loancalculator.domain;

public class InvalidLoanException extends Exception {

    public InvalidLoanException(String message) {
        super(message);
    }
}
