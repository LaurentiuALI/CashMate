package com.example.CashMate.dtos;

import com.example.CashMate.data.Recursion;
import com.example.CashMate.data.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private Long id;
    private Long account_id;
    private String name;
    private String description;
    private Double amount;
    private Date date;
    private Type type;
    private Recursion recursion;

}
