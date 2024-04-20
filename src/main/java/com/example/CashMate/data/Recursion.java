package com.example.CashMate.data;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="recursion")
public class Recursion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="date")
    private Date date;

    @OneToOne
    private Transaction transaction;

    @Override
    public String toString() {
        return "Recursion{" +
                "id=" + id +
                ", date=" + date +
                ", transaction=" + transaction +
                '}';
    }

    public Recursion(Long id, Date date) {
        this.id = id;
        this.date = date;
    }

    public Recursion() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recursion recursion = (Recursion) o;
        return id == recursion.id && Objects.equals(date, recursion.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
