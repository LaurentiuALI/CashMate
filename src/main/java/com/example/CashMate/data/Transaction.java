package com.example.CashMate.data;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_id")
    private Long account_id;

    public Transaction(Long id, Long account_id) {
        this.id = id;
        this.account_id = account_id;
    }

    public Transaction() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        Transaction that = (Transaction) o;
        return id == that.id && account_id == that.account_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account_id);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", account_id=" + account_id +
                '}';
    }
}

//    CREATE TABLE transaction (
//        id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
//        account_id BIGINT NOT NULL,
//        name varchar(50) not null,
//        description varchar(255),
//        amount double precision not null,
//        date DATETIME not null,
//        recursion_id BIGINT NOT NULL,
//        type ENUM('EXPENSE', 'INCOME') NOT NULL,
//
//        FOREIGN KEY (recursion_id) REFERENCES recursion(id) ON DELETE CASCADE,
//        FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
//        );