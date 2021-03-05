package com.db.homework.entity;


import lombok.Data;

@Data
public class Book {
    private int id;
    private Category category;
    private String cover;
    private String title;
    private String author;
    private String date;
    private String press;
    private String abs;
    //store
    private int total;
    private int borrowed;
    //reservation
    private String time;
    private Boolean canReserve; //can_reserve
    //loan
    private String loanDate; // loan_date
    private String dueDate; // due_date
    //status
    // 0--not reserve
    // 1-- reserved
    // 2-- loan
    private int status;
}
