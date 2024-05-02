package com.example.CashMate.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class AccountNotCreatedException extends RuntimeException {
    public AccountNotCreatedException() {
    }

    public AccountNotCreatedException(String message) {
        super(message);
    }

    public AccountNotCreatedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
