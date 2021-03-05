package com.db.homework.service;

import com.db.homework.entity.Menu;
import com.db.homework.entity.User;

import java.util.List;

public interface MenuService {
    //18
    List<Menu> getBackMenu(User user);

    void handleMenus(List<Menu> menus);
    //38
    List<Menu> getMenus();
}
