package com.example.CashMate.repositories.security;

import com.example.CashMate.data.security.CashUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CashUserRepository extends JpaRepository<CashUser, Long> {

    List<CashUser> findAll();

    List<CashUser> findByName(String name);

}
