package com.example.CashMate.data;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

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

    @Column(name = "date")
    private Date date;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private Recursion recursion;

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", account_id=" + account_id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", type=" + type +
                ", recursion=" + recursion.getId() +
                '}';
    }


    public Transaction(Long id, Long account_id, String name, String description, Double amount, Type type, Date date) {
        this.id = id;
        this.account_id = account_id;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

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


    public Transaction(Long id, Long account_id, String name, String description, Double amount, Type type) {
        this.id = id;
        this.account_id = account_id;
        this.name = name;
        this.description = description;
        this.amount = amount;
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
        return id.equals(that.id) && account_id.equals(that.account_id) && name.equals(that.name) && description.equals(that.description) && amount.equals(that.amount) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account_id, name, description, amount, type);
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

