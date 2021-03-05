package com.db.homework.controller;


import com.db.homework.entity.Student;
import com.db.homework.log.OperLog;
import com.db.homework.result.Result;
import com.db.homework.result.ResultFactory;
import com.db.homework.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.beans.Transient;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class StudentController {
    @Autowired
    StudentService studentService;
    //29
    @GetMapping("/back/user/students")
    @OperLog(operModul = "后台-用户管理",operType = "GET",operDesc = "获得学生信息")
    public Result getStudents() throws Exception{
        return ResultFactory.buildSuccessResult(studentService.getAllStudents());
    }
    //30
    @PostMapping("/back/user/students/add")
    @OperLog(operModul = "后台-用户管理",operType = "POST",operDesc = "添加借阅证")
    public Result addCardNo(@RequestBody Student student) throws Exception{
        return ResultFactory.buildSuccessResult(studentService.addCardNo(student));
    }

    //31
    @PostMapping("/back/user/students/delete")
    @OperLog(operModul = "后台-用户管理",operType = "POST",operDesc = "删除借阅证")
    public Result deleteCardNo(@RequestBody Student student) throws Exception{
        return ResultFactory.buildSuccessResult(studentService.deleteCardNo(student));
    }


    //32
    @Transient
    @PostMapping("/back/user/students/addStudent")
    @OperLog(operModul = "后台-用户管理",operType = "POST",operDesc = "添加学生")
    public Result addStudent(@RequestBody Student student) throws Exception{
        return ResultFactory.buildSuccessResult(studentService.addStudent(student));
    }
}
