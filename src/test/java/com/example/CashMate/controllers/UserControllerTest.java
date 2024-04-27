package com.example.CashMate.controllers;

import com.example.CashMate.dtos.CashUserDTO;
import com.example.CashMate.services.CashUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    Model model;

    @Mock
    CashUserService cashUserService;

    @InjectMocks
    CashUserController cashUserController;

    @Test
    public void showById() {
        CashUserDTO newUserDTO = new CashUserDTO();
        newUserDTO.setName("testUser");
        newUserDTO.setPassword("password123");

        when(cashUserService.createAccount(newUserDTO)).thenReturn(newUserDTO);

        String viewName = cashUserController.register(newUserDTO);

        assertEquals(viewName, "main");
    }

}
