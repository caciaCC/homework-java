package com.db.homework.entity;


import lombok.Data;

import java.util.List;

@Data
public class Role {
   private int id;
   private String name;
   private String nameCn;
   private boolean enabled;
   private String des;
   //34
   List<Menu> menus;
   List<Permission> perms;
}
