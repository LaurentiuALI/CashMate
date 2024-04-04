package com.example.CashMate.data;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserAccountId implements Serializable {

    @Column(name="user_id")
    private long user_id;

    @Column(name="account_id")
    private long account_id;

    public UserAccountId(long user_id, long account_id) {
        this.user_id = user_id;
        this.account_id = account_id;
    }

    public UserAccountId() {
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccountId that = (UserAccountId) o;
        return user_id == that.user_id && account_id == that.account_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, account_id);
    }

    @Override
    public String toString() {
        return "UserAccountId{" +
                "user_id=" + user_id +
                ", account_id=" + account_id +
                '}';
    }
}
