package com.db.homework.entity;


import lombok.Data;

@Data
public class LoanResponse {
    private int id;
    private String message;
    private String cardNo;
    private int bid;
    private String loanDate;
    private String dueDate;
    private String optTime;


    private String backDate;
    private String fine;

    //51
    private String ok;
    private Book book;
}
