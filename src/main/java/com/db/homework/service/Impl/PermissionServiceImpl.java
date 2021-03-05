package com.db.homework.service.Impl;

import com.db.homework.entity.Menu;
import com.db.homework.entity.Permission;
import com.db.homework.mapper.PermissionMapper;
import com.db.homework.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl  implements PermissionService {
    @Autowired
    PermissionMapper permissionMapper;
    //37
    @Override
    public List<Permission> getPerms() {
        return permissionMapper.getAllPerms();
    }
}
