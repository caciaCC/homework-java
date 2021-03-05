package com.db.homework.controller;

import com.db.homework.entity.Student;
import com.db.homework.entity.User;
import com.db.homework.log.OperLog;
import com.db.homework.result.Result;
import com.db.homework.result.ResultFactory;
import com.db.homework.service.StudentService;
import com.db.homework.service.UserService;
import com.db.homework.utils.RrandomNumber;
import com.db.homework.utils.StringAndDate;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.beans.Transient;
import java.text.ParseException;
import java.util.Date;


@RestController
@CrossOrigin
@RequestMapping("/api")
public class LoginController {
    @Autowired
    UserService userService;
    @Autowired
    StudentService studentService;
    //8.{user.cardno,user.password}
    @PostMapping("/login")
    @OperLog(operModul = "前台",operType = "POST",operDesc = "登录")
//    public Result login(@RequestBody User user)
//    {
//        String cardno = user.getCardNo();
//        cardno = HtmlUtils.htmlEscape(cardno);
//        boolean success = userService.existCardnoPassword(user.getCardNo(),user.getPassword());
//        if(!success)  {
//            return ResultFactory.buildFailResult("借阅证号或密码错误!");
//        }
//        userService.setStatus(cardno,true, new Date());
//        return ResultFactory.buildSuccessResult(user);
//    }
    public Result login(@RequestBody User requestUser) {
        String username = requestUser.getCardNo();
        Subject subject = SecurityUtils.getSubject();
//        subject.getSession().setTimeout(10000);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, requestUser.getPassword());
        try {
            subject.login(usernamePasswordToken);
//            System.out.println(1111111111);
            return ResultFactory.buildSuccessResult(username);
        } catch (AuthenticationException e) {
            String message = "账号密码错误";
            return ResultFactory.buildFailResult(message);
        }
    }
    //9.{student.sno,student.password}
    @Transient
    @PostMapping("/register")
    @OperLog(operModul = "前台",operType = "POST",operDesc = "注册")
    public Result register(@RequestBody Student student) throws ParseException {
        String sno = student.getSno();
        sno =HtmlUtils.htmlEscape(sno);
        String password = student.getPassword();
        Student studentInDB = studentService.getBySnoAndPassword(sno,password);
        if(studentInDB==null){
            return ResultFactory.buildFailResult("学号不存在或密码错误!");
        }
        if(studentInDB.getRegistered()){
            return ResultFactory.buildFailResult("借阅证已存在!");
        }
        //生成一个用户到数据库
        User user = new User();
        String cardno;
        // TODO 唯一cardno生成策略
        while(true){
            cardno = RrandomNumber.getRandomNumber(10);
            boolean existCardno = userService.isExistCardno(cardno);
            if(!existCardno) break;
        }
        user.setCardNo(cardno);
        user.setPassword(studentInDB.getPassword());
        user.setSalt(studentInDB.getSalt());
        user.setEnabled(true);
        user.setStatus(false);
        user.setLastTime(StringAndDate.getDetailedString(new Date()));
        userService.addOne(user);
        studentInDB.setRegistered(true);
        studentService.updateRegistered(studentInDB);
        studentService.updateCardNo(studentInDB.getId(),cardno);
        return ResultFactory.buildSuccessResult(user);
    }
    // 10.{user.cardno}
    @PostMapping("/logout")
    @OperLog(operModul = "前台",operType = "POST",operDesc = "登出")
    public Result logout(@RequestBody User user)
    {
        String cardno = user.getCardNo();
        cardno = HtmlUtils.htmlEscape(cardno);
        User userInDB = userService.getUser(cardno);
        if(userInDB==null)  {
            return ResultFactory.buildFailResult("不存在这个用户!");
        }
//        if(!userInDB.getStatus()){
//            return ResultFactory.buildFailResult("用户未登录!");
//        }
        userService.setStatus(cardno,false, new Date());
        return ResultFactory.buildSuccessResult(user);
    }
//    // 11 {user.cardno}
//    @PostMapping("/identify")
//    @OperLog(operModul = "前台",operType = "POST",operDesc = "验证登录")
//    public Result identify(@RequestBody User user)
//    {
//        String cardno = user.getCardNo();
//        cardno = HtmlUtils.htmlEscape(cardno);
//        User userInDB = userService.getUser(cardno);
//        if(userInDB==null)  {
//            return ResultFactory.buildFailResult("认证失败!");
//        }
//        if(!userInDB.getStatus()){
//            return ResultFactory.buildFailResult("用户未登录!");
//        }
//        return ResultFactory.buildSuccessResult(user);
//    }
    //49
    @GetMapping("/authentication")
    public String authentication() {
//        System.out.println(111);
        return "身份认证成功";
    }
}
