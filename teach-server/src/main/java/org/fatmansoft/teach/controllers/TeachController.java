package org.fatmansoft.teach.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.CourseRepository;
import org.fatmansoft.teach.repository.SelectRepository;
import org.fatmansoft.teach.repository.TeacherRepository;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")

public class TeachController {
    @Autowired
    private CourseRepository  courseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SelectRepository selectRepository;

    @PostMapping("/getCourseList")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse getCourseList(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId= CommonMethod.getUserId();
        User u=userRepository.getById(userId);
        Teacher t;
        if(u.getUserType().getName().equals(EUserType.ROLE_ADMIN))t=u.getTeacher();
        else t=teacherRepository.getById(114514);
        String courseNum = dataRequest.getString("num");
        String courseName = dataRequest.getString("name");
        List list = courseRepository.findAll();
        List dataList = new ArrayList();
        if(list == null || list.size()== 0) return CommonMethod.getReturnData(dataList);

        Map m;
        Course c;
        if(courseNum!=null&& !courseNum.equals("")){
            c=courseRepository.findByCourseNum(courseNum).get();
            m=new HashMap();
            m.put("courseId",c.getCourseId());
            m.put("courseNum",c.getCourseNum());
            m.put("courseName",c.getCourseName());
            m.put("credit",c.getCredit());
            m.put("start",toDateString(c.getStart()));
            m.put("end",toDateString(c.getEnd()));
            m.put("time",getTimeString(c.getTime()));
            m.put("editable",String.valueOf(c.getTeacher().equals(t)));
            m.put("teacherName",c.getTeacher().getName());
            m.put("description",c.getDescription());
            dataList.add(m);
            return CommonMethod.getReturnData(dataList);
        }else if(courseName!=null&& !courseName.equals("")){
            list=courseRepository.findCoursesByCourseName(courseName);
            if(list == null || list.size()== 0) return CommonMethod.getReturnData(dataList);
        }
        for(int i = 0; i < list.size();i++) {
            c = (Course) list.get(i);
            m = new HashMap();
            m.put("courseId",c.getCourseId());
            m.put("courseNum",c.getCourseNum());
            m.put("courseName",c.getCourseName());
            m.put("credit",c.getCredit());
            m.put("start",toDateString(c.getStart()));
            m.put("end",toDateString(c.getEnd()));
            m.put("time",getTimeString(c.getTime()));
            m.put("editable",String.valueOf(c.getTeacher().equals(t)));
            m.put("teacherName",c.getTeacher().getName());
            m.put("description",c.getDescription());
            dataList.add(m);
        }
        return CommonMethod.getReturnData(dataList);
    }
    @PostMapping("/getCourseInfo")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse getCourseInfo(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId= CommonMethod.getUserId();
        User u=userRepository.findByUserId(userId).get();
        if(u.getUserType().getName()==EUserType.ROLE_USER)return CommonMethod.getReturnMessageError("无权限");
        Integer courseId = dataRequest.getInteger("courseId");
        Course c= null;
        Optional<Course> op;
        if(courseId != null) {
            op= courseRepository.findById(courseId);
            if(op.isPresent()) {
                c = op.get();
            }
        }
        Map form = new HashMap();
        List timeList=new ArrayList();
        if(c != null) {
            form.put("teacherId",c.getTeacher().getTeacherId());
            form.put("courseId",c.getCourseId());
            form.put("courseNum",c.getCourseNum());
            form.put("courseName",c.getCourseName());
            form.put("description",c.getDescription());
            form.put("start",c.getStart());
            form.put("end",c.getEnd());
            form.put("credit",c.getCredit());
            int time=0;
            try{time=c.getTime();}catch (Exception e){e.printStackTrace();}
            for(int i=1;i<=25;i++)
                    if((time&(1<<i))>0)timeList.add(String.valueOf(i));
        }else form.put("teacherId", u.getTeacher().getTeacherId());
        Map res=new HashMap();
        res.put("form",form);
        res.put("time",timeList);
        return CommonMethod.getReturnData(res);
    }

    //  课程信息提交
    @PostMapping("/courseSubmit")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse courseSubmit(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId= CommonMethod.getUserId();
        Map form = dataRequest.getMap("form");
        Integer courseId = (Integer)form.get("courseId");
        String courseNum = (String)form.get("courseNum");
        if(courseNum.equals(""))return CommonMethod.getReturnMessageError("课程号不得为空！");
        if(courseRepository.findCoursesByCourseNum(courseNum).size()>1)return CommonMethod.getReturnMessageError("课程号已经存在");
        String courseName = (String)form.get("courseName");
        String description=(String)form.get("description");
        Integer teacherId=(Integer) form.get("teacherId");
        Integer credit;
        if(form.get("credit").getClass()==String.class)
            credit= Integer.parseInt((String)form.get("credit"));
        else credit=(Integer)form.get("credit");
        if(credit<0)return CommonMethod.getReturnMessageError("学分需为正数！");
        List timeList=dataRequest.getList("time");
        Integer time=0;
        for(int i=0;i<timeList.size();i++){
            int bit=Integer.parseInt((String)timeList.get(i));
            time+=1<<bit;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date start=null,end=null;
        Calendar cal=Calendar.getInstance();

        try {
            start=format.parse((String)form.get("start"));
            cal.setTime(start);
            cal.add(Calendar.DATE,1);
            start=cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            end=format.parse((String)form.get("end"));
            cal.setTime(end);
            cal.add(Calendar.DATE,1);
            end=cal.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Course c= null;
        Optional<Course> op;
        if(courseId != null) {
            op= courseRepository.findById(courseId);
            if(op.isPresent()) {
                c = op.get();
            }
        }
        if(c == null) {
            c = new Course();
        }
        c.setCourseNum(courseNum);
        c.setCourseName(courseName);
        c.setDescription(description);
        c.setStart(start);
        c.setEnd(end);
        c.setCredit(credit);
        c.setTime(time);
        c.setTeacher(teacherRepository.getById(teacherId));
        courseRepository.save(c);
        return CommonMethod.getReturnData(c.getCourseId());
    }

    //  课程信息删除
    @PostMapping("/courseDelete")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse courseDelete(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId= CommonMethod.getUserId();
        Integer courseId = dataRequest.getInteger("courseId");
        Course c= null;
        Optional<Course> op;
        if(courseId != null) {
            op= courseRepository.findById(courseId);
            if(op.isPresent()) {
                c = op.get();
            }
        }
        if(c != null) {
            List sList=selectRepository.getSelectsByCourse(c);
            for(int i=0;i<sList.size();i++)
                selectRepository.delete((Select)sList.get(i));
            courseRepository.delete(c);
        }
        return CommonMethod.getReturnMessageOK();
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
