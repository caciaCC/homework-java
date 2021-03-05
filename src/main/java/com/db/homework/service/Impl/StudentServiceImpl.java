package com.db.homework.service.Impl;

import com.db.homework.entity.Student;
import com.db.homework.entity.User;
import com.db.homework.mapper.StudentMapper;
import com.db.homework.mapper.UserMapper;
import com.db.homework.service.StudentService;
import com.db.homework.utils.RrandomNumber;
import com.db.homework.utils.StringAndDate;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class StudentServiceImpl  implements StudentService {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    UserMapper userMapper;
    //9
    @Override
    public Student getBySnoAndPassword(String sno, String password) {
        return studentMapper.getOneBySnoAndPassword(sno,password);
    }
    //9
    @Override
    public void updateRegistered(Student studentInDB) {
        studentMapper.updateRegistered(studentInDB.getId(),studentInDB.getRegistered());
    }

    @Override
    public void updateCardNo(int id, String cardno) {
        studentMapper.updateCardNo(id, cardno);
    }
    //29
    @Override
    public List<Student> getAllStudents() {
        List<Student> students = studentMapper.getTotalList();
        for(Student student: students){
            if(!student.getRegistered()) student.setCardNo("未生成");
        }
        return students;
    }
   //30
    @Override
    public Student addCardNo(Student student) throws ParseException {
        Student studentInDB = studentMapper.getById(student.getId());
        //生成用户
        User user = new User();
        String cardno;
        // TODO 唯一cardno生成策略
        while(true){
            cardno = RrandomNumber.getRandomNumber(10);
            boolean existCardno = (userMapper.getOneByCardno(cardno) != null);
            if(!existCardno) break;
        }
        user.setCardNo(cardno);
        user.setPassword(studentInDB.getPassword());
        user.setSalt(studentInDB.getSalt());
        user.setEnabled(true);
        user.setStatus(false);
        user.setLastTime(StringAndDate.getDetailedString(new Date()));
        userMapper.addOne(user.getCardNo(),user.getPassword(),user.getSalt(),user.getEnabled(),user.getStatus(),user.getLastTime());

        studentMapper.updateCardNo(student.getId(),cardno);
        studentMapper.updateRegistered(student.getId(),true);
        return student;
    }
    //31
    @Override
    public Student deleteCardNo(Student student) {
        studentMapper.updateRegistered(student.getId(),false);
        userMapper.deleteCardNo(student.getCardNo());
        return student;
    }
    //32
    @Override
    public Student addStudent(Student student) {
        String password = student.getPassword();
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5",password,salt,times).toString();
        student.setPassword(encodedPassword);
        student.setSalt(salt);
        studentMapper.addStudent(student.getSno(),student.getPassword(),student.getSalt(),student.getName(),student.getDepartment(),student.getPhone(),false);
        return student;
    }
}
