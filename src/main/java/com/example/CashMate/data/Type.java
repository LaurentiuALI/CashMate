package com.example.CashMate.data;

public enum Type {
     INCOME("INCOME"), EXPENSE("EXPENSE");
    private String description;

    public String getDescription(){
        return description;
    }

    Type(String description){
        this.description = description;
    }
}
