package com.db.homework.entity;

import lombok.Data;

@Data
public class ReservationResponse {
    private  int id;
    private boolean status;
    private String reason;
    private String reserveTime;
    private String optTime;
    private String cardNo;
    private int bid;
    //50
    Book book;
    private String ok;
}
