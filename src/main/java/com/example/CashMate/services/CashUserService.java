package com.example.CashMate.services;

import com.example.CashMate.data.Account;
import com.example.CashMate.data.security.CashUser;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.CashUserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface CashUserService {

    CashUserDTO getByName(String name);
    CashUserDTO addAccount(String name, AccountDTO accountDto);
    CashUserDTO createAccount(CashUserDTO cashUserDTO);

    List<CashUser> getAll();

    CashUserDTO existsByName(String name);
}
