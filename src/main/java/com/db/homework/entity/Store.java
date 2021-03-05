package com.db.homework.entity;

import lombok.Data;

@Data
public class Store {
    private int id;
    private int bid;
    private int total;
    private int borrowed;
    private String lastTime;
    //26
    private int left;
    private String name;
    //28
    private int change;
    private int borrowedChange;
}
