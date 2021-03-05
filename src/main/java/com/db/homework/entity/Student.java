package com.db.homework.entity;


import lombok.Data;

@Data
public class Student {
    private int id;
    private String sno;
    private String password;
    private String salt;
    private String name;
    private String department;
    private String phone;
    private Boolean registered;
    private String cardNo;
}
