package com.db.homework.entity;


import lombok.Data;

@Data
public class Loan {
    private int id;
    private String cardNo;  //card_no
    private int bid;
    private String loanDate; //loan_date
    private String dueDate; // due_date
    private String backDate;
    private int status; // 0 -- 待归还 1 -- 待罚款     2--待确认    3--已确认
    //
    private String overDue;
    //
    private Category category;
    //
    private String overDate;
    //
    private String fine;

    // 25
    private String nowOverDue;
    private String phone;
    private int style;
}
