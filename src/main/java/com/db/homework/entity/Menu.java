package com.db.homework.entity;

import lombok.Data;

import java.util.List;

@Data
public class Menu {
    private int id;
    private String path;
    private String name;
    private String nameCn;
    private String icon;
    private String component;
    private int pid;
    //
    private List<Menu> children;
}
