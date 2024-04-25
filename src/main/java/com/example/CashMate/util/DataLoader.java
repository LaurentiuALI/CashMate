package com.example.CashMate.util;

import com.example.CashMate.data.Account;
import com.example.CashMate.data.security.Authority;
import com.example.CashMate.data.security.CashUser;
import com.example.CashMate.dtos.AccountDTO;
import com.example.CashMate.repositories.AccountRepository;
import com.example.CashMate.repositories.security.AuthorityRepository;
import com.example.CashMate.repositories.security.CashUserRepository;
import com.example.CashMate.services.CashUserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Component
@Profile("mysql")
public class DataLoader implements CommandLineRunner {

    private AuthorityRepository authorityRepository;
    private CashUserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AccountRepository accountRepository;
    private CashUserService cashUserService;


    private void loadUserData() {
        if (userRepository.count() == 0){
            Authority adminRole = authorityRepository.save(Authority.builder().role("ROLE_ADMIN").build());
            Authority guestRole = authorityRepository.save(Authority.builder().role("ROLE_GUEST").build());

            CashUser admin = CashUser.builder()
                    .name("admin")
                    .password(passwordEncoder.encode("12345"))
                    .authority(adminRole)
                    .build();

            CashUser guest = CashUser.builder()
                    .name("guest")
                    .password(passwordEncoder.encode("12345"))
                    .authority(guestRole)
                    .build();

            userRepository.save(admin);
            userRepository.save(guest);


            admin = userRepository.findByName("admin").get(0);
            AccountDTO account = new AccountDTO(1L, "Testing", admin.getId(), new HashSet<>(), admin.getName());

            cashUserService.addAccount(admin.getName(), account);

        }
    }


    @Override
    public void run(String... args) throws Exception {
        loadUserData();
    }
}