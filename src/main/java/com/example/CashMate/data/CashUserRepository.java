package com.example.CashMate.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashUserRepository extends CrudRepository<CashUser, Long> {

//    @Query("SELECT DISTINCT user FROM User user JOIN FETCH user.accounts acccounts")
    List<CashUser> findAll();
}
