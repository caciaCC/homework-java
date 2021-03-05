package com.db.homework.service.Impl;

import com.db.homework.mapper.RoleMenuMapper;
import com.db.homework.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleMenuServiceImpl  implements RoleMenuService {
    @Autowired
    RoleMenuMapper roleMenuMapper;
    @Override
    public List<Integer> getMidsByRid(Integer rid) {
        return roleMenuMapper.getMidsByRid(rid);
    }
}
