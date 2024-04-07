package com.example.CashMate.util;

import com.example.CashMate.data.CashUserRepository;
import org.springframework.stereotype.Component;


@Component
public class UserGeneralChecks {

    CashUserRepository cashUserRepository;

    public UserGeneralChecks(CashUserRepository cashUserRepository) {
        this.cashUserRepository = cashUserRepository;
    }

    public boolean checkIfUserExists(long userID) {
        return cashUserRepository.existsById(userID);
    }

    public void userValidityCheck(long userID) {
        if (!checkIfUserExists(userID)) {
            throw new IllegalArgumentException("User does not exist");
        }
    }

}
