package com.db.homework.entity;


import lombok.Data;

@Data
public class Log {
    private int id;
    private String modul;
    private String type;
    private String descp;
    private String method;
    private String uri;
    private String ip;
    private String time;
}
