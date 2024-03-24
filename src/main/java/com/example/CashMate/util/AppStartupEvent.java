package com.example.CashMate.util;

import com.example.CashMate.data.User;
import com.example.CashMate.data.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AppStartupEvent implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;

    public AppStartupEvent(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        Iterable<User> users = userRepository.findAll();
        users.forEach(System.out::println);
    }
}
