package com.example.CashMate.dtos;


import com.example.CashMate.data.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private Long id;
    private String name;
    private Long user_id;
    private Set<Transaction> transactions;
    private String ownerName;

}
