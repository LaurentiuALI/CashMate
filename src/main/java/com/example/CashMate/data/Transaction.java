package com.example.CashMate.data;

import jakarta.annotation.Nullable;
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

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id.equals(that.id) && account.equals(that.account) && name.equals(that.name) && Objects.equals(description, that.description) && amount.equals(that.amount) && date.equals(that.date) && type == that.type && Objects.equals(recursion, that.recursion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, name, description, amount, date, type, recursion);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", type=" + type +
                ", recursion=" + recursion.getId() +
                '}';
    }


    public Transaction(Long id, Account account, String name, String description, Double amount, Type type, Date date) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public Transaction(Long id, Account account, String name, String description, Double amount, Type type) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.type = type;
    }

    public Transaction(Long id, Account account) {
        this.id = id;
        this.account = account;
    }

    public Transaction() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
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

