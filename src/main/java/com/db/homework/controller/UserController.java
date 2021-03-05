package com.db.homework.controller;

import com.db.homework.entity.Role;
import com.db.homework.entity.User;
import com.db.homework.log.OperLog;
import com.db.homework.result.Result;
import com.db.homework.result.ResultFactory;
import com.db.homework.service.MenuService;
import com.db.homework.service.PermissionService;
import com.db.homework.service.RoleService;
import com.db.homework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.beans.Transient;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    PermissionService permissionService;
    @Autowired
    MenuService menuService;
    //33
    @GetMapping("/back/user/users")
    @OperLog(operModul = "后台-用户管理",operType = "GET",operDesc = "获得用户信息")
    public Result getUsers() throws Exception{
        return ResultFactory.buildSuccessResult(userService.getAllUsers());
    }
    //34
    @GetMapping("/back/user/roles")
    @OperLog(operModul = "后台-用户管理",operType = "GET",operDesc = "获得角色信息")
    public Result getRoles() throws Exception{
        return ResultFactory.buildSuccessResult(roleService.getRoles());
    }
    //35
    @PostMapping("/back/user/users/add")
    @OperLog(operModul = "后台-用户管理",operType = "POST",operDesc = "添加用户")
    public Result addUser(@RequestBody User user) throws ParseException {
        return userService.addUser(user);
    }

    //36
    @PostMapping("/back/user/users/delete")
    @OperLog(operModul = "后台-用户管理",operType = "POST",operDesc = "删除用户")
    public Result deleteUser(@RequestBody User user) throws ParseException {
        return userService.deleteUser(user);
    }

    //37
    @GetMapping("/back/user/perms")
    @OperLog(operModul = "后台-用户管理",operType = "GET",operDesc = "获得权限信息")
    public Result getPerms() throws Exception{
        return ResultFactory.buildSuccessResult(permissionService.getPerms());
    }

    //38
    @GetMapping("/back/user/menus")
    @OperLog(operModul = "后台-用户管理",operType = "GET",operDesc = "获得菜单信息")
    public Result getMenus() throws Exception{
        return ResultFactory.buildSuccessResult(menuService.getMenus());
    }
    //39
    @PutMapping("/back/user/users/put")
    @OperLog(operModul = "后台-用户管理",operType = "PUT",operDesc = "修改用户")
    public Result putUser(@RequestBody User user) throws Exception {
        return ResultFactory.buildSuccessResult(userService.putUsers(user));
    }

    //40
    @Transient
    @PostMapping("/back/user/roles/add")
    @OperLog(operModul = "后台-用户管理",operType = "POST",operDesc = "添加角色")
    public Result addRole(@RequestBody Role role) throws ParseException {
        return roleService.add(role);
    }


    //41

    @PutMapping("/back/user/roles/put")
    @OperLog(operModul = "后台-用户管理",operType = "PUT",operDesc = "修改角色")
    public Result putRole(@RequestBody Role role) throws Exception {
        return ResultFactory.buildSuccessResult(roleService.putRole(role));
    }
   //42
   @PutMapping("/back/user/roles/menu")
   @OperLog(operModul = "后台-用户管理",operType = "PUT",operDesc = "修改菜单")
   public Result updateRoleMenu(@RequestParam int rid, @RequestBody LinkedHashMap menusIds) {
       roleService.updateRoleMenu(rid, menusIds);
       return ResultFactory.buildSuccessResult("RM修改成功");
   }

    //43
    @Transient
    @PostMapping("/back/user/roles/delete")
    @OperLog(operModul = "后台-用户管理",operType = "POST",operDesc = "删除角色")
    public Result deleteRole(@RequestBody Role role) throws ParseException {
        roleService.deleteRole(role.getId());
        return ResultFactory.buildSuccessResult("Role删除成功");
    }
}
