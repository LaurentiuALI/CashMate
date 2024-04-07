package com.example.CashMate.data;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.*;

@Entity
@Table(name="cash_user")
public class CashUser {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="name")
    private String name;

    @Column(name="password")
    private String password;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private Set<Account> accounts;

    public void addAccount(Account account){
        if(this.accounts == null){
            this.accounts = new HashSet<>();
        }
        accounts.add(account);
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) { this.accounts = accounts; }

    public void setId(Long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CashUser(long id, String name, String password, Set<Account> accounts) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.accounts = accounts;
    }

    public CashUser(String name, String password, Set<Account> accounts) {
        this.name = name;
        this.password = password;
        this.accounts = accounts;
    }

    public CashUser(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public CashUser(long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
    public CashUser() {
    }

    @Override
    public String toString() {
        return "cash_user={" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
