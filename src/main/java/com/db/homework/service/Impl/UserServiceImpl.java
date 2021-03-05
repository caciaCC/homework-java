package com.db.homework.service.Impl;

import com.db.homework.entity.Permission;
import com.db.homework.entity.Role;
import com.db.homework.entity.User;
import com.db.homework.mapper.*;
import com.db.homework.result.Result;
import com.db.homework.result.ResultFactory;
import com.db.homework.service.UserService;
import com.db.homework.utils.StringAndDate;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired UserMapper userMapper;
    @Autowired UserRoleMapper userRoleMapper;
    @Autowired RoleMapper roleMapper;
    @Autowired StudentMapper studentMapper;
    @Autowired
    PermissionMapper permissionMapper;
    @Autowired
    RolePermissionMapper rolePermissionMapper;

    //8
    @Override
    public boolean existCardnoPassword(String cardno, String password) {
        return userMapper.getOneByCardnoPassword(cardno,password)!=null;
    }
    //9
    @Override
    public boolean isExistCardno(String cardno) {
        return (User)userMapper.getOneByCardno(cardno)!=null;
    }
    //9
    @Override
    public void addOne(User user) {
        userMapper.addOne(user.getCardNo(),user.getPassword(),user.getSalt(),user.getEnabled(),user.getStatus(), user.getLastTime());
    }
    //8
    @Override
    public void setStatus(String cardno, Boolean status, Date date) {
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        String lastTime = sdf.format(date);
        userMapper.setStatus(cardno,status,lastTime);
    }

    @Override
    public User getUser(String cardno) {
        return userMapper.getOneByCardno(cardno);
    }
    //33
    @Override
    public List<User> getAllUsers() {
         List<User> users = userMapper.getAllUsers();
         for(User user: users) {
            List<Integer> rids = userRoleMapper.getRidsByUid(user.getId());
            List<Role> roles = rids.stream().map(rid-> roleMapper.getRoleByRid(rid)).collect(Collectors.toList());
             while (roles.remove(null));
             user.setRoles(roles);
         }
         return users;
    }
    //35
    @Override
    public Result addUser(User user) throws ParseException {
        String CardNo = user.getCardNo();
        if(userMapper.getOneByCardno(CardNo) != null){
            return ResultFactory.buildFailResult("数据库中已存在该卡号!");
        }
        String password = user.getPassword();
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5",password,salt,times).toString();
        userMapper.addOne(CardNo,encodedPassword,salt,true,false, StringAndDate.getDetailedString(new Date()));

        return ResultFactory.buildSuccessResult(user);
    }
    //36
    @Override
    public Result deleteUser(User user) {
        userMapper.deleteCardNo(user.getCardNo());
        studentMapper.updateAllCardNo(user.getCardNo());
        return ResultFactory.buildSuccessResult(user);
    }
    //39
    @Override
    public User putUsers(User user) {
        //更新用户
        //userService.updateUser(user);
        //用u.username找到u.id
        User userInDB = userMapper.getOneByCardno(user.getCardNo());
        int uid = userInDB.getId();
        List<Role> roles = user.getRoles();
        List<Integer> nowrids = new ArrayList<>();
        //用r.id和u.id建U-R表
        for(Role role: roles)
        {
            int rid = role.getId();
            nowrids.add(rid);
            if(userRoleMapper.containUidRid(uid,rid) == null)
                userRoleMapper.insertUidRid(uid,rid);
        }
        //用之前的RIDS和现在的RIDS删U-R表
        List<Integer> recentRids = userRoleMapper.getRidsByUid(uid);
        for(Integer recentrid: recentRids)
        {
//            System.out.println(recentrid);
            boolean flag = false;
            for(Integer nowrid: nowrids)
            {
                if (recentrid.compareTo(nowrid) == 0) {
                    flag = true;
                    break;
                }
            }
            if(!flag)
            {
                userRoleMapper.deleteUidRid(uid,recentrid);
            }

        }
        return user;
    }

    @Override
    public boolean needFilter(String requestAPI) {
        List<Permission> ps = permissionMapper.getAllPerms();
            for (Permission p: ps) {
                if (requestAPI.startsWith(p.getUrl())) {
                    return true;
                }
            }
            return false;
    }

    @Override
    public Set<String> listPermissionURLsByUser(String username) {
        User user = userMapper.getOneByCardno(username);
        List<Integer> rids = userRoleMapper.getRidsByUid(user.getId());
//        List<Role> roles = rids.stream().map(rid-> roleMapper.getRoleByRid(rid)).collect(Collectors.toList());
        List<Integer> pids = new ArrayList<>();
        for(Integer rid: rids){
            List<Integer> tmppids = rolePermissionMapper.getPidsByRid(rid);
            pids.addAll(tmppids);
        }
//        List<Integer> pids = rolePermissionMapper.getPidsByRid(role.getId());
        pids = pids.stream().distinct().collect(Collectors.toList());
        List<Permission> permissions = pids.stream().map(pid-> permissionMapper.getPermsByPid(pid)).collect(Collectors.toList());
        Set<String> URLs = permissions.stream().map(Permission::getUrl).collect(Collectors.toSet());
        return URLs;
    }
}
