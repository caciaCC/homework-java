package com.db.homework.service.Impl;

import com.db.homework.entity.Menu;
import com.db.homework.entity.User;
import com.db.homework.mapper.MenuMapper;
import com.db.homework.service.MenuService;
import com.db.homework.service.RoleMenuService;
import com.db.homework.service.UserRoleService;
import com.db.homework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    MenuMapper menuMapper;

    @Autowired
    UserService userService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    RoleMenuService roleMenuService;
    //18
    @Override
    public List<Menu> getBackMenu(User user) {
        String cardNo = user.getCardNo();
        cardNo = HtmlUtils.htmlEscape(cardNo);
        User userInDB = userService.getUser(cardNo);
        int uid = userInDB.getId();
        List<Integer> rids = userRoleService.getRidsByUid(uid);
        List<Integer> mids = new ArrayList<>();
        for(Integer rid: rids){
            List<Integer> midsList = roleMenuService.getMidsByRid(rid);
            if(midsList!=null) mids.addAll(midsList);
        }
        mids = mids.stream().distinct().collect(Collectors.toList());
//        for(Integer mid : mids){
//            System.out.println(mid);
//
//        }
        List<Menu> menus = mids.stream().map(mid-> menuMapper.getMenuByMid(mid)).collect(Collectors.toList());
//        for(Menu m: menus){
//            System.out.println(m.getId());
//        }
        if(!menus.isEmpty())
            handleMenus(menus);
//        for(Menu m: menus){
//            System.out.println(m.toString());
//        }
        return menus;
    }

    public void handleMenus(List<Menu> menus) {
        menus.forEach(menu -> {
            if(menu != null){
                List<Menu> children = menuMapper.getMenusByPid(menu.getId());
                if(children != null)
                    menu.setChildren(children);
            }
        });
        menus.removeIf(m->m.getPid()!=0);
    }
    //38
    @Override
    public List<Menu> getMenus() {
        return menuMapper.getAllMenus();
    }
}
