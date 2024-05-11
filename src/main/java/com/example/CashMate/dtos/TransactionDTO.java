package com.example.CashMate.dtos;

import com.example.CashMate.data.Category;
import com.example.CashMate.data.Recursion;
import com.example.CashMate.data.Type;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private Long id;
    private Long account_id;
    @NotBlank(message="The name cannot be empty.")
    private String name;
    private String description;
    @Min(value = 0, message = "The value must be greater than 0.01")
    private Double amount;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Date must be in the past")
    private Date date;
    private Type type;
    private Recursion recursion;

    private List<Category> categoriesList;
}
