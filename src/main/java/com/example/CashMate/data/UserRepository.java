package com.example.CashMate.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

//    @Query("SELECT DISTINCT user FROM User user JOIN FETCH user.accounts acccounts")
    List<User> findAll();
}
