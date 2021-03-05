package com.db.homework.entity;

import lombok.Data;

@Data
public class Reservation {
    private int id;
    private String cardNo; // card_no
    private int bid;
    private String time;
    //
    private int total;
    private int left;
    //
    private String reason;
}
