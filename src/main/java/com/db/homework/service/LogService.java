package com.db.homework.service;

import com.db.homework.entity.Log;

import java.util.List;

public interface LogService {
    //44
    void insert(Log log);
    //47
    List<Log> getTotalList();
}
