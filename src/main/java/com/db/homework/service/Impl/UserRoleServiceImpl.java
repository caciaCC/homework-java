package com.db.homework.service.Impl;


import com.db.homework.mapper.UserRoleMapper;
import com.db.homework.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl  implements UserRoleService {
    @Autowired
    UserRoleMapper userRoleMapper;
    @Override
    public List<Integer> getRidsByUid(int uid) {
        return userRoleMapper.getRidsByUid(uid);
    }
}
