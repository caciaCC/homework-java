package com.db.homework.entity;

import lombok.Data;

@Data
public class ExceptionLog {
    private int id;
    private String name;
    private String message;
    private String method;
    private String uri;
    private String ip;
    private String time;
}
