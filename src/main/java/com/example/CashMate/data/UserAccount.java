package com.example.CashMate.data;

import com.example.CashMate.data.security.CashUser;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="user_account")
public class UserAccount {

    @EmbeddedId
    private UserAccountId id;


    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name="user_id")
    private CashUser cashUser;

    @ManyToOne
    @MapsId("account_id")
    @JoinColumn(name="account_id")
    private Account account;

    public UserAccount(UserAccountId userAccountId) {
        this.id = userAccountId;
    }

    public UserAccount(UserAccountId id, CashUser cashUser, Account account) {
        this.id = id;
        this.cashUser = cashUser;
        this.account = account;
    }

    public UserAccount() {
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", user=" + cashUser +
                ", account=" + account +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return id.equals(that.id) && cashUser.equals(that.cashUser) && account.equals(that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cashUser, account);
    }

    public UserAccountId getId() {
        return id;
    }

    public void setId(UserAccountId id) {
        this.id = id;
    }

    public CashUser getCashUser() {
        return cashUser;
    }

    public void setCashUser(CashUser cashUser) {
        this.cashUser = cashUser;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
//    CREATE TABLE user_account (
//        user_id BIGINT NOT NULL,
//        account_id BIGINT NOT NULL,
//
//        FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
//        FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE,
//        PRIMARY KEY(user_id, account_id)
//        );