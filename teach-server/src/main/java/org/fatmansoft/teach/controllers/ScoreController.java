package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.logging.ClasspathLoggingApplicationListener;
import org.springframework.data.relational.core.sql.In;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.Class;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/score")
public class ScoreController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    SelectRepository selectRepository;

    @PostMapping("/getStudents")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse getStudents(@Valid @RequestBody DataRequest dataRequest) {
        Integer courseId = dataRequest.getInteger("courseId");
        Course c=courseRepository.getById(courseId);
        List sList=selectRepository.getSelectsByCourse(c);
        List dataList=new ArrayList();
        if(sList==null||sList.size()==0)return CommonMethod.getReturnData(dataList);
        for(int i=0;i<sList.size();i++){
            Select s=(Select)sList.get(i);
            Map m=new HashMap();
            m.put("selectId",s.getSelectId());
            m.put("sid",s.getStudent().getSid());
            m.put("name",s.getStudent().getName());
            m.put("score",s.getScore());
            m.put("valid",s.isValid());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/submitScore")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse submitScore(@Valid @RequestBody DataRequest dataRequest) {
        List sList=dataRequest.getList("score");
        Boolean valid= dataRequest.getInteger("valid") == 1;
        for(int i=0;i<sList.size();i++){
            Map m=(Map)sList.get(i);
            Select s=selectRepository.getById((Integer)m.get("selectId"));
            double score=0;
            try{
                Class<?> aClass = m.get("score").getClass();
                if (String.class.equals(aClass)) score = Double.parseDouble((String) m.get("score"));
                else if (Integer.class.equals(aClass)) score = (Integer) m.get("score");
                else if (Double.class.equals(aClass)) score = (Double) m.get("score");
            }
            catch (Exception e){return CommonMethod.getReturnMessageError("请不要给出数字以外的字符");}
            if(score>100||score<0)return CommonMethod.getReturnMessageError("分数不应大于100分或小于0分");
            s.setScore(score);
            s.setValid(valid);
            selectRepository.save(s);
        }
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/getScores")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse getScores(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId= CommonMethod.getUserId();
        User u=userRepository.findByUserId(userId).get();
        Student student=u.getStudent();
        List list = selectRepository.getSelectsByStudent(student);
        List dataList = new ArrayList();
        if(list == null || list.size()== 0) return CommonMethod.getReturnData(dataList);

        Map m;
        Course c;
        Select s;
        for(int i = 0; i < list.size();i++) {
            s=(Select)list.get(i);
            if(s.isValid()) {
                c = s.getCourse();
                m = new HashMap();
                m.put("courseNum", c.getCourseNum());
                m.put("courseName", c.getCourseName());
                m.put("credit", c.getCredit());
                m.put("score", s.getScore());
                m.put("rank",getRank(s,false));
                m.put("rankInClass",getRank(s,true));
                dataList.add(m);
            }
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/getGPA")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse getGPA(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId= CommonMethod.getUserId();
        User u=userRepository.findByUserId(userId).get();
        Student student=u.getStudent();
        List list = selectRepository.getSelectsByStudent(student);
        Select s;
        double credit=0,score=0;
        for(int i = 0; i < list.size();i++) {
            s = (Select) list.get(i);
            if(s.isValid()&&s.getScore()>=60) {
                score += (s.getScore()-50)*s.getCourse().getCredit()/50;
                credit += s.getCourse().getCredit();
            }
        }
        return CommonMethod.getReturnData(score/credit*5);
    }

    private int getRank(Select select,boolean inClass){
        Course c=select.getCourse();
        Student s=select.getStudent();
        List list=selectRepository.getSelectsByCourse(c);
        for(int i=0;i<list.size();i++){
            Select sel=(Select)list.get(i);
            Student stu=sel.getStudent();
            if((inClass&&!stu.getCls().equals(s.getCls()))||!sel.isValid())
                sel.setScore(0);
            list.set(i,sel);
        }

        list.sort((Comparator<Select>) (o1, o2) -> {
            if (o1.equals(o2)) return 0;
            return o1.getScore() < o2.getScore() ? 1 : -1;
        });
        return list.indexOf(select)+1;
    }
}