package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.*;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/select")
public class SelectController {
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

    @PostMapping("/getSelected")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse getSelected(@Valid @RequestBody DataRequest dataRequest) {
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
            c = s.getCourse();
            m = new HashMap();
            m.put("courseId",c.getCourseId());
            m.put("courseNum",c.getCourseNum());
            m.put("courseName",c.getCourseName());
            m.put("credit",c.getCredit());
            m.put("start",toDateString(c.getStart()));
            m.put("end",toDateString(c.getEnd()));
            m.put("time",getTimeString(c.getTime()));
            m.put("teacherName",c.getTeacher().getName());
            m.put("description",c.getDescription());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }

    @PostMapping("/select")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse select(@Valid @RequestBody DataRequest dataRequest) {
        Integer courseId = dataRequest.getInteger("courseId");
        Course c=courseRepository.getById(courseId);

        Date date=addOneDay(new Date());
        if(c.getStart()!=null&&c.getEnd()!=null) {
            if (date.after(c.getEnd())) return CommonMethod.getReturnMessageError("这门课已经结束选课");
            if (date.before(c.getStart())) return CommonMethod.getReturnMessageError("这门课尚未开始选课");
        }

        Integer userId= CommonMethod.getUserId();
        User u=userRepository.findByUserId(userId).get();
        Student student=u.getStudent();
        List list = selectRepository.getSelectsByStudent(student);
        Select s;
        for(int i = 0; i < list.size();i++) {
            s=(Select)list.get(i);
            Course c1=s.getCourse();
            if(c1.equals(c))
                return CommonMethod.getReturnMessageError("已经选择此课程");
            if((c1.getTime()&c.getTime())>0)
                return CommonMethod.getReturnMessageError("与已选课程\""+c1.getCourseNum()+":"+c1.getCourseName()+"\"时间冲突！");
        }
        Select select=new Select();
        select.setStudent(student);
        select.setCourse(c);
        selectRepository.save(select);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/unselect")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse unselect(@Valid @RequestBody DataRequest dataRequest) {
        Integer courseId = dataRequest.getInteger("courseId");
        Course c=courseRepository.getById(courseId);
        Date date=addOneDay(new Date());
        if(c.getEnd()!=null)
            if (date.after(c.getEnd())) return CommonMethod.getReturnMessageError("这门课已经结束选课");

        Integer userId= CommonMethod.getUserId();
        User u=userRepository.findByUserId(userId).get();
        Student student=u.getStudent();
        Select sel=selectRepository.getSelectsByStudentAndCourse(student,c).get();
        selectRepository.delete(sel);
        return CommonMethod.getReturnMessageOK();
    }

    @PostMapping("/getWeekly")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse getWeekly(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId= CommonMethod.getUserId();
        User u=userRepository.findByUserId(userId).get();
        Student student=u.getStudent();
        List list = selectRepository.getSelectsByStudent(student);
        List cList=new ArrayList();
        List dataList = new ArrayList();
        Course c;
        Select s;
        for(int i = 0; i < list.size();i++) {
            s=(Select)list.get(i);
            cList.add(s.getCourse());
        }
        for(int i=1;i<=5;i++){
            Map m=new HashMap();
            for(int j=1;j<=5;j++){
                int tmp=(j-1)*5+i;
                for(int k=0;k<cList.size();k++){
                    c=(Course)cList.get(k);
                    if((c.getTime()&(1<<tmp))>0)m.put(String.valueOf(j),c.getCourseName());
                }
            }
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);

    }

    private Date addOneDay(Date date){
        Calendar   calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE,1);
        return calendar.getTime();
    }
    private String getTimeString(int time){
        String st="";
        for(int i=1;i<=25;i++)
            if((time&(1<<i))>0)st+=" 周"+((i-1)/5+1)+"第"+((i-1)%5+1)+"节 ;";
        return st;
    }
    private String toDateString(Date date){
        if(date==null)date=new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }


}