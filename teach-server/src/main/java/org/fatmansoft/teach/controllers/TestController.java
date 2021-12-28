package org.fatmansoft.teach.controllers;

import org.fatmansoft.teach.models.EUserType;
import org.fatmansoft.teach.models.User;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/teach")
public class TestController {
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userAccess(HttpSession session) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return "User Content.";
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> adminAccess(HttpSession session) {

        LocalDateTime ts = (LocalDateTime)session.getAttribute("ts");

        Map<String, String> res = new HashMap<String, String>();

        if (ts == null) {
            res.put("ts", "");
        } else {
            res.put("ts", ts.toString());
        }
        session.setAttribute("ts",  LocalDateTime.now());
        return ResponseEntity.ok(new DataResponse(
                "ok",
                res
        ));
    }
    @PostMapping("/getMenuList")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse getMenuList(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId= CommonMethod.getUserId();
        User user;
        Optional<User> tmp = userRepository.findByUserId(userId);
        user = tmp.get();
        List mList = new ArrayList();
        Map m = new HashMap();
        m.put("name","ChangePassword");
        m.put("title","修改密码");
        mList.add(m);


        if(user.getUserType().getName()== EUserType.ROLE_ADMIN) {
            m = new HashMap();
            m.put("name","CourseTable");
            m.put("title","课程管理");
            mList.add(m);

            m = new HashMap();
            m.put("name", "TeacherProfile");
            m.put("title", "个人资料");
            mList.add(m);
        }else{
            m=new HashMap();
            m.put("name","StudentProfile");
            m.put("title","个人资料");
            mList.add(m);

            m=new HashMap();
            m.put("name","CourseSelection");
            m.put("title","选课");
            mList.add(m);

            m=new HashMap();
            m.put("name","WeeklyTable");
            m.put("title","查看课表");
            mList.add(m);

            m=new HashMap();
            m.put("name","CheckScore");
            m.put("title","成绩查询");
            mList.add(m);
        }
        return CommonMethod.getReturnData(mList);
    }
    @PostMapping("/getMenuListHtml")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse getMenuListHtml(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId= CommonMethod.getUserId();
        User user;
        Optional<User> tmp = userRepository.findByUserId(userId);
        user = tmp.get();
        List mList = new ArrayList();
        Map m = new HashMap();
        m.put("name","ChangePassword");
        m.put("title","修改密码");
        mList.add(m);
        m = new HashMap();
        m.put("name","CourseTable");
        m.put("title","课程管理");
        mList.add(m);
        m = new HashMap();
        m.put("name","DashSample");
        m.put("title","测试");
        mList.add(m);
        Map data = new HashMap();
        data.put("mList", mList);
        data.put("uerName", user.getUserName());
        return CommonMethod.getReturnData(data);
    }
    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DataResponse changePassword(@Valid @RequestBody DataRequest dataRequest) {
        Integer userId= CommonMethod.getUserId();
        if(userId == null)
            return CommonMethod.getReturnMessageError("lang.comm.loginError");
        String oldPassword = dataRequest.getString("oldPassword");
        String newPassword = dataRequest.getString("newPassword");
        User u = userRepository.findById(userId).get();
        if(!encoder.matches(oldPassword, u.getPassword())) {
            return CommonMethod.getReturnMessageError("原密码错误！");
        }
        u.setPassword(encoder.encode(newPassword));
        userRepository.save(u);
        return CommonMethod.getReturnMessageOK();
    }

}