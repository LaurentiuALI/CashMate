package com.example.CashMate.data;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="transaction_category")
public class TransactionCategory {
    @EmbeddedId
    private TransactionCategoryId id;

    @ManyToOne
    @MapsId("transaction_id")
    @JoinColumn(name="transaction_id")
    private Transaction transaction;

    public TransactionCategory(TransactionCategoryId id, Transaction transaction, Category category) {
        this.id = id;
        this.transaction = transaction;
        this.category = category;
    }

    public TransactionCategory() {
    }

    public TransactionCategoryId getId() {
        return id;
    }

    public void setId(TransactionCategoryId id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "TransactionCategory{" +
                "id=" + id +
                ", transaction=" + transaction +
                ", category=" + category +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionCategory that = (TransactionCategory) o;
        return Objects.equals(id, that.id) && Objects.equals(transaction, that.transaction) && Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transaction, category);
    }

    @ManyToOne
    @MapsId("category_id")
    @JoinColumn(name="category_id")
    private Category category;
}