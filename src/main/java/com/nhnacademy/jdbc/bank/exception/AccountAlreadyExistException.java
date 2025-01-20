package com.nhnacademy.jdbc.bank.exception;

public class AccountAlreadyExistException extends RuntimeException {
    public AccountAlreadyExistException(long accountNumber) {
        super("account aready exist accountNumber : " + accountNumber);
    }
}
