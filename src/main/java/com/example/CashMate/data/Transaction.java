package com.example.CashMate.data;

import jakarta.persistence.*;

import java.util.Date;
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

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "recursion_id")
    private Long recursion_id;

    @Column(name = "type")
    private Type type;

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", account_id=" + account_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", recursion_id=" + recursion_id +
                ", type=" + type +
                ", date=" + date +
                '}';
    }

    public Transaction(Long id, Long account_id, String name, String description, Double amount, Long recursion_id, Type type, Date date) {
        this.id = id;
        this.account_id = account_id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.recursion_id = recursion_id;
        this.type = type;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "date")
    private Date date;

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


    public Transaction(Long id, Long account_id, String name, String description, Double amount, Long recursion_id, Type type) {
        this.id = id;
        this.account_id = account_id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.recursion_id = recursion_id;
        this.type = type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getRecursion_id() {
        return recursion_id;
    }

    public void setRecursion_id(Long recursion_id) {
        this.recursion_id = recursion_id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id.equals(that.id) && account_id.equals(that.account_id) && name.equals(that.name) && description.equals(that.description) && amount.equals(that.amount) && Objects.equals(recursion_id, that.recursion_id) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account_id, name, description, amount, recursion_id, type);
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

