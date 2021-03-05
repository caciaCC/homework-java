package com.db.homework.service.Impl;

import com.db.homework.entity.Log;
import com.db.homework.mapper.LogMapper;
import com.db.homework.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    LogMapper logMapper;
    @Override
    //44
    public void insert(Log log) {
        logMapper.insert(log.getModul(),log.getType(),log.getDescp(),log.getMethod(),log.getUri(),log.getIp(),log.getTime());
    }
    //47
    @Override
    public List<Log> getTotalList() {
        return logMapper.getTotalList();
    }
}
