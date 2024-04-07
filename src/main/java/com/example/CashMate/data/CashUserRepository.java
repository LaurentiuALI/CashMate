package com.example.CashMate.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CashUserRepository extends CrudRepository<CashUser, Long> {

//    @Query("SELECT DISTINCT user FROM CashUser user JOIN FETCH user.accounts acccounts")
    List<CashUser> findAll();

    List<CashUser> findByName(String name);
}
