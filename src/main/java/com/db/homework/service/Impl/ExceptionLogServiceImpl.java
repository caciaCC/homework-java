package com.db.homework.service.Impl;

import com.db.homework.entity.ExceptionLog;
import com.db.homework.entity.Log;
import com.db.homework.mapper.ExceptionLogMapper;
import com.db.homework.service.ExceptionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExceptionLogServiceImpl  implements ExceptionLogService {
    @Autowired
    ExceptionLogMapper exceptionLogMapper;
    @Override
    //45
    public void insert(ExceptionLog excepLog) {
            exceptionLogMapper.insert(excepLog.getName(),excepLog.getMessage(),excepLog.getMethod(),excepLog.getUri(),excepLog.getIp(),excepLog.getTime());
    }
    //48
    @Override
    public List<ExceptionLog> getTotalList() {
         return exceptionLogMapper.getTotalList();
    }
}
