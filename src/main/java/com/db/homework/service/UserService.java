package com.db.homework.service;

import com.db.homework.entity.Role;
import com.db.homework.entity.User;
import com.db.homework.result.Result;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface UserService {
    //8
    boolean existCardnoPassword(String cardno, String password);
    //9
    boolean isExistCardno(String cardno);
    //9
    void addOne(User user);
    //8  //10
    void setStatus(String cardno, Boolean status, Date date);
    //10
    User getUser(String cardno);
    //33
    List<User> getAllUsers();
    //35
    Result addUser(User user) throws ParseException;
    //36
    Result deleteUser(User user);
    //39
    User putUsers(User user);
    //?
    public boolean needFilter(String requestAPI);
    //?
    Set<String> listPermissionURLsByUser(String username);
}
