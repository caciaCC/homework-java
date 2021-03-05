package com.db.homework.service;

import com.db.homework.entity.Student;

import java.text.ParseException;
import java.util.List;

public interface StudentService {
    //9
    Student getBySnoAndPassword(String sno, String password);
    //9
    void updateRegistered(Student studentInDB);
    //9
    void updateCardNo(int id, String cardno);
    //29
    List<Student> getAllStudents();
    //30
    Student addCardNo(Student student) throws ParseException;
    //31
    Student deleteCardNo(Student student);
    //32
    Student addStudent(Student student);
}
