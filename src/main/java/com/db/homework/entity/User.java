package com.db.homework.entity;

import lombok.Data;

import java.util.List;


@Data
public class User {
    private int id;
    private String cardNo; // card_no
    private String password;
    private String salt;
    private Boolean enabled;
    private Boolean status;
    private String lastTime;
    //Reservation
    private int bid;
    private String time;
    //keywords
    private String key;
    //33
    List<Role> roles;
}
