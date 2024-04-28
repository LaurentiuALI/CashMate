package com.example.CashMate.services;

import com.example.CashMate.data.Account;
import com.example.CashMate.data.security.Authority;
import com.example.CashMate.data.security.CashUser;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.exceptions.ResourceAlreadyExistsException;
import com.example.CashMate.repositories.AccountRepository;
import com.example.CashMate.repositories.security.AuthorityRepository;
import com.example.CashMate.repositories.security.CashUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CashUserServiceImpl implements CashUserService{

    @PersistenceContext
    private EntityManager entityManager;

    private CashUserRepository cashUserRepository;
    private AccountRepository accountRepository;
    private AuthorityRepository authorityRepository;

    private PasswordEncoder passwordEncoder;

    private ModelMapper modelMapper;

    public CashUserServiceImpl(CashUserRepository cashUserRepository, AccountRepository accountRepository,
                               ModelMapper modelMapper, PasswordEncoder passwordEncoder,
                               AuthorityRepository authorityRepository){

        this.cashUserRepository = cashUserRepository;
        this.accountRepository = accountRepository;
        this.authorityRepository = authorityRepository;

        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CashUserDTO getByName(String name){

        CashUser user = cashUserRepository.findByName(name).get(0);
        if (user == null){
            throw new RuntimeException("User not found!");
        }
        return modelMapper.map(user, CashUserDTO.class);
    }

    public List<CashUser> getAll(){
        return cashUserRepository.findAll();
    }

    @Override
    @Transactional
    public CashUserDTO addAccount(String name, AccountDTO accountDto){

        CashUser user = cashUserRepository.findByName(name).get(0);

        accountDto.setUser_id(user.getId());
        Account account = modelMapper.map(accountDto, Account.class);
        accountRepository.save(account);

        return modelMapper.map(user, CashUserDTO.class);
    }

    @Override
    public CashUserDTO createAccount(CashUserDTO cashUserDTO) {


        if(!cashUserRepository.findByName(cashUserDTO.getName()).isEmpty()){
            throw new ResourceAlreadyExistsException("The user with name " + cashUserDTO.getName() + " already exists.");
        }

        Authority guestRole = authorityRepository.save(Authority.builder().role("ROLE_GUEST").build());

        CashUser user = CashUser.builder()
                .name(cashUserDTO.getName())
                .password(passwordEncoder.encode(cashUserDTO.getPassword()))
                .authority(guestRole)
                .build();
        cashUserRepository.save(user);

        return cashUserDTO;
    }

    @Transactional
    public CashUser updateName(Long userId, String name){
        CashUser updatedUser = entityManager.find(CashUser.class, userId);
        updatedUser.setName(name);
        entityManager.persist(updatedUser);
        return updatedUser;
    }

    @Override
    public CashUserDTO existsByName(String name){
        CashUser user = cashUserRepository.findByName(name).get(0);
        if (user == null){
            throw new RuntimeException("User not found!");
        }
        return modelMapper.map(user, CashUserDTO.class);
    }


}
