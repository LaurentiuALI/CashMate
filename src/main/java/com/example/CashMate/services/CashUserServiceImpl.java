package com.example.CashMate.services;

import com.example.CashMate.data.Account;
import com.example.CashMate.data.security.CashUser;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.repositories.AccountRepository;
import com.example.CashMate.repositories.security.CashUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CashUserServiceImpl implements CashUserService{

    @PersistenceContext
    private EntityManager entityManager;

    private CashUserRepository cashUserRepository;
    private AccountRepository accountRepository;

    private ModelMapper modelMapper;

    public CashUserServiceImpl(CashUserRepository cashUserRepository, AccountRepository accountRepository, ModelMapper modelMapper){
        this.cashUserRepository = cashUserRepository;
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CashUserDTO getByName(String name){

        CashUser user = cashUserRepository.findByName(name).get(0);
        if (user == null){
            throw new RuntimeException("User not found!");
        }
        return modelMapper.map(user, CashUserDTO.class);
    }

    @Override
    @Transactional
    public CashUserDTO addAccount(String name, AccountDTO accountDto){

        CashUser user = cashUserRepository.findByName(name).get(0);

        accountDto.setUser_id(user.getId());
        Account account = modelMapper.map(accountDto, Account.class);
//        user.addAccount(account);
        accountRepository.save(account);

        return modelMapper.map(user, CashUserDTO.class);
    }
    @Transactional
    public CashUser updateName(Long userId, String name){
        CashUser updatedUser = entityManager.find(CashUser.class, userId);
        updatedUser.setName(name);
        entityManager.persist(updatedUser);
        return updatedUser;
    }


}
