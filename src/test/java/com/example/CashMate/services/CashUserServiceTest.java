package com.example.CashMate.services;

import com.example.CashMate.data.security.Authority;
import com.example.CashMate.data.security.CashUser;
import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.exceptions.CashUserNotFoundException;
import com.example.CashMate.repositories.security.AuthorityRepository;
import com.example.CashMate.repositories.security.CashUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CashUserServiceTest {

    @Mock
    private CashUserRepository cashUserRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CashUserServiceImpl cashUserService;

    @Test
    public void createNewUser() {
        CashUserDTO newUserDTO = new CashUserDTO();
        newUserDTO.setName("newUser");
        newUserDTO.setPassword("password");

        when(cashUserRepository.findByName(newUserDTO.getName())).thenReturn(Collections.emptyList());
        when(authorityRepository.save(any())).thenReturn(new Authority());
        when(passwordEncoder.encode(newUserDTO.getPassword())).thenReturn("encodedPassword");

        CashUserDTO result = cashUserService.createAccount(newUserDTO);

        verify(cashUserRepository, times(1)).save(any());
        assertSame(newUserDTO, result);
    }

    @Test
    public void existingUser() {
        CashUserDTO existingUserDTO = new CashUserDTO();
        existingUserDTO.setName("existingUser");
        existingUserDTO.setPassword("password");

        when(cashUserRepository.findByName(existingUserDTO.getName())).thenReturn(List.of(new CashUser()));

        assertThrows(CashUserNotFoundException.class, () -> cashUserService.createAccount(existingUserDTO));

        verify(cashUserRepository, never()).save(any());
    }


    @Test
    public void verifyCreateAccountData() {
        CashUserDTO newUserDTO = new CashUserDTO();
        newUserDTO.setName("newUser");
        newUserDTO.setPassword("password");

        when(cashUserRepository.findByName(newUserDTO.getName())).thenReturn(Collections.emptyList());
        when(authorityRepository.save(any())).thenReturn(new Authority());
        when(passwordEncoder.encode(newUserDTO.getPassword())).thenReturn("encodedPassword");

        cashUserService.createAccount(newUserDTO);

        ArgumentCaptor<CashUser> userCaptor = ArgumentCaptor.forClass(CashUser.class);
        verify(cashUserRepository).save(userCaptor.capture());
        CashUser savedUser = userCaptor.getValue();

        assertEquals(newUserDTO.getName(), savedUser.getName());
        assertEquals("encodedPassword", savedUser.getPassword());
    }
    // Additional tests for verifying correct data is saved can be added here
}

