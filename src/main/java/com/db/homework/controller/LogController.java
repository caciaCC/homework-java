package com.db.homework.controller;


import com.db.homework.entity.ExceptionLog;
import com.db.homework.log.OperLog;
import com.db.homework.result.Result;
import com.db.homework.result.ResultFactory;
import com.db.homework.service.ExceptionLogService;
import com.db.homework.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class LogController {
    @Autowired
    LogService logService;
    @Autowired
    ExceptionLogService exceptionLogService;
    //47
    @GetMapping("/back/system/logs")
    @OperLog(operModul = "后台-系统管理",operType = "GET",operDesc = "查询日志列表")
    public Result getTotalListLog() throws Exception {
        return ResultFactory.buildSuccessResult(logService.getTotalList());
    }

    //48
    @GetMapping("/back/system/exceptionLogs")
    @OperLog(operModul = "后台-系统管理",operType = "GET",operDesc = "查询异常日志列表")
    public Result getTotalListExceptionLog() throws Exception {
        return ResultFactory.buildSuccessResult(exceptionLogService.getTotalList());
    }
}
