package com.db.homework.service.Impl;

import com.db.homework.entity.Menu;
import com.db.homework.entity.Permission;
import com.db.homework.entity.Role;
import com.db.homework.mapper.*;
import com.db.homework.result.Result;
import com.db.homework.result.ResultFactory;
import com.db.homework.service.MenuService;
import com.db.homework.service.RolePermissionService;
import com.db.homework.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RoleMenuMapper roleMenuMapper;
    @Autowired
    MenuService menuService;
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    PermissionMapper permissionMapper;

    @Autowired
    RolePermissionMapper rolePermissionMapper;
    //34
    @Override
    public List<Role> getRoles() {
        List<Role> roles = roleMapper.getAllRoles();

        for(Role role: roles)
        {
            List<Integer> mids = roleMenuMapper.getMidsByRid(role.getId());
            mids = mids.stream().distinct().collect(Collectors.toList());
            List<Menu> menus = mids.stream().map(mid-> menuMapper.getMenuByMid(mid)).collect(Collectors.toList());


            List<Integer> pids = rolePermissionMapper.getPidsByRid(role.getId());
            pids = pids.stream().distinct().collect(Collectors.toList());
            List<Permission> permissions = pids.stream().map(pid-> permissionMapper.getPermsByPid(pid)).collect(Collectors.toList());
            while (menus.remove(null));
            while (permissions.remove(null));
            if(!menus.isEmpty())
            {
                menuService.handleMenus(menus);
            }
            role.setMenus(menus);
            role.setPerms(permissions);
        }
        //获得对应的permission
        // 我觉得对应关系是R - RP - P;√
        // 要有一个通过 role.id获得Permission表的方法.->注意role拥有List<Permission>
        //1.增加List<Permission>
        //2.然后写一个类似上面对Menu的操作的方法
        //3.然后可以将其整合到上面的Role循环里
        //4.前端去除对应注释，检验编辑框中是否勾选来验证成功。
            return roles;
    }
    //40
    @Override
    public Result add(Role role) {
        roleMapper.add(role.getName(),role.getNameCn(),true,role.getDes());
        return ResultFactory.buildSuccessResult(role);
    }
    //41
    @Override
    public Role putRole(Role role) {
        roleMapper.update(role.getId(),role.getName(),role.getNameCn(),role.isEnabled(),role.getDes());
        // 感觉updateRole需要
        // 1.更新Role ID NAME NAME_ZH ENABLED
        // 2.更新Perms => 通过更新 RP表（增 删)
        // 3.无 => 因为更新Menu是后续操作
        //找到r.id
        int rid = role.getId();
        List<Permission> permissions = role.getPerms();
        List<Integer> nowpids = new ArrayList<>();
        //用r.id和p.id建R-P表
        for(Permission perm: permissions)
        {
            int pid = perm.getId();
            nowpids.add(pid);
//            System.out.println(rid);
            if(rolePermissionMapper.containRidPid(rid,pid)==null)
                rolePermissionMapper.insertRidPid(rid,pid);
        }
        //用之前的PIDS和现在的PIDS删R-P表
        List<Integer> recentPids = rolePermissionMapper.getPidsByRid(rid);
        for(Integer recentpid: recentPids)
        {
//            System.out.println(recentrid);
            boolean flag = false;
            for(Integer nowpid: nowpids)
            {
                if (recentpid.compareTo(nowpid) == 0) {
                    flag = true;
                    break;
                }
            }
            if(!flag)
            {
                rolePermissionMapper.deleteRidPid(rid,recentpid);
            }

        }
        return role;
    }
    //42
    @Override
    public void updateRoleMenu(int rid, LinkedHashMap menusIds) {
        roleMenuMapper.deleteRidMidsByRid(rid);
//        List<RoleMenu> rms = new ArrayList<>();
        for (Integer mid : (List<Integer>)menusIds.get("menusIds")) {
//            RoleMenu rm = new RoleMenu();
//            rm.setMid(mid);
//            rm.setRid(rid);
            //   rms.add(rm);
            roleMenuMapper.insertRidMid(rid, mid);
        }
    }
    //43
    @Override
    public void deleteRole(int id) {
        roleMapper.deleteRole(id);
    }
}
