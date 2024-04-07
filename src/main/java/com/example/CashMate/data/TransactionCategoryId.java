package com.example.CashMate.data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TransactionCategoryId implements Serializable {

    @Column(name="transaction_id")
    private Long transaction_id;

    @Column(name="category_id")
    private Long category_id;

    public TransactionCategoryId(Long transaction_id, Long category_id) {
        this.transaction_id = transaction_id;
        this.category_id = category_id;
    }

    public TransactionCategoryId() {
    }

    @Override
    public String toString() {
        return "TransactionCategoryId{" +
                "transaction_id=" + transaction_id +
                ", category_id=" + category_id +
                '}';
    }

    public Long getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(Long transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionCategoryId that = (TransactionCategoryId) o;
        return Objects.equals(transaction_id, that.transaction_id) && Objects.equals(category_id, that.category_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transaction_id, category_id);
    }
}


//    CREATE TABLE transaction_category (
//        transaction_id BIGINT NOT NULL,
//        category_id BIGINT NOT NULL,
//
//        FOREIGN KEY (transaction_id) REFERENCES transaction(id) ON DELETE CASCADE,
//        FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE,
//        PRIMARY KEY(transaction_id, category_id)
//        );