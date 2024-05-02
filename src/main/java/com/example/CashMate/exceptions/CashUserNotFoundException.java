package com.example.CashMate.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class CashUserNotFoundException extends RuntimeException {
    public CashUserNotFoundException() {
    }

    public CashUserNotFoundException(String message) {
        super(message);
    }

    public CashUserNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
