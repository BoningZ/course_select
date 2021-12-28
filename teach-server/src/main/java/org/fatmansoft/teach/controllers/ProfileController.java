package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.EUserType;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.Teacher;
import org.fatmansoft.teach.models.User;
import org.fatmansoft.teach.models.Class;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.ClassRepository;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.TeacherRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class ProfileController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    ClassRepository classRepository;

    @PostMapping("/getProfile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse getProfile(@Valid @RequestBody DataRequest dataRequest){
        Integer userId= CommonMethod.getUserId();
        User user;
        Optional<User> tmp = userRepository.findByUserId(userId);
        user = tmp.get();
        Map m=new HashMap();
        if(user.getUserType().getName()== EUserType.ROLE_ADMIN){
            Teacher teacher=user.getTeacher();
            m.put("tid",teacher.getTid());
            m.put("name",teacher.getName());
        }else{
            Student student=user.getStudent();
            m.put("sid",student.getSid());
            m.put("name",student.getName());
            m.put("addr",student.getAddr());
            m.put("birthday",student.getBirthday());
            m.put("email",student.getEmail());
            try {
                m.put("class", student.getCls().getClassId());
            }catch (Exception e){e.printStackTrace();}
            m.put("grade",student.getGrade());
            m.put("major",student.getMajor());
            m.put("tel",student.getTel());
            List classList=new ArrayList();
            List cList=classRepository.findAll();
            for(int i=0;i<cList.size();i++){
                Class c=(Class)cList.get(i);
                Map classMap=new HashMap();
                classMap.put("value",c.getClassId());
                classMap.put("label",c.getName());
                classList.add(classMap);
            }
            m.put("classes",classList);
        }
        return CommonMethod.getReturnData(m);
    }

    @PostMapping("/submitProfile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse submitProfile(@Valid @RequestBody DataRequest dataRequest){
        Integer userId= CommonMethod.getUserId();
        User user;
        Optional<User> tmp = userRepository.findByUserId(userId);
        user = tmp.get();
        if(user.getUserType().getName()== EUserType.ROLE_ADMIN){
            Teacher teacher=user.getTeacher();
            teacher.setTid(dataRequest.getString("tid"));
            teacher.setName(dataRequest.getString("name"));
            teacherRepository.save(teacher);
        }else{
            Student student=user.getStudent();
            student.setSid(dataRequest.getString("sid"));
            student.setName(dataRequest.getString("name"));
            student.setAddr(dataRequest.getString("addr"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date birth=null;
            Calendar cal=Calendar.getInstance();
            try {
                birth=format.parse(dataRequest.getString("birthday"));
                cal.setTime(birth);
                cal.add(Calendar.DATE,1);
                birth=cal.getTime();
            } catch (ParseException e) {e.printStackTrace();}
            student.setBirthday(birth);
            student.setEmail(dataRequest.getString("email"));
            try {
                student.setCls(classRepository.getById(dataRequest.getInteger("class")));
            }catch (Exception e){e.printStackTrace();}
            student.setGrade(dataRequest.getInteger("grade"));
            student.setMajor(dataRequest.getString("major"));
            student.setTel(dataRequest.getString("tel"));
            studentRepository.save(student);
        }
        return CommonMethod.getReturnMessageOK();
    }

}

