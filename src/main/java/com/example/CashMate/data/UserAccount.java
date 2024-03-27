package com.example.CashMate.data;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="user_account")
public class UserAccount {

    @EmbeddedId
    private UserAccountId id;


    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("account_id")
    private Account account;

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", user=" + user +
                ", account=" + account +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return id.equals(that.id) && user.equals(that.user) && account.equals(that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, account);
    }

    public UserAccountId getId() {
        return id;
    }

    public void setId(UserAccountId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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