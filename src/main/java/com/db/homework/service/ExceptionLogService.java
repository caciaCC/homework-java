package com.db.homework.service;

import com.db.homework.entity.ExceptionLog;
import com.db.homework.entity.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExceptionLogService  {
    //45
    void insert(ExceptionLog excepLog);
    //48
    List<ExceptionLog> getTotalList();
}
