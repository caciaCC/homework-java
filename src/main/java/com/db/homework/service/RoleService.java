package com.db.homework.service;

import com.db.homework.entity.Role;
import com.db.homework.result.Result;

import java.util.LinkedHashMap;
import java.util.List;

public interface RoleService {
    //34
    List<Role> getRoles();
    //40
    Result add(Role role);
    //41
    Role putRole(Role role);
   //42
    void updateRoleMenu(int rid, LinkedHashMap menusIds);
    //43
    void deleteRole(int id);
}
