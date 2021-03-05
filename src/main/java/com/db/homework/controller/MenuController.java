package com.db.homework.controller;


import com.db.homework.entity.User;
import com.db.homework.log.OperLog;
import com.db.homework.result.Result;
import com.db.homework.result.ResultFactory;
import com.db.homework.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class MenuController {
    @Autowired
    MenuService menuService;
    //18
    @PostMapping("/back/menu")
    @OperLog(operModul = "后台",operType = "POST",operDesc = "获得后台菜单")
    public Result getBackMenu(@RequestBody User user)
    {
//        System.out.println(user.getCardNo());
        return ResultFactory.buildSuccessResult(menuService.getBackMenu(user));
    }
}
