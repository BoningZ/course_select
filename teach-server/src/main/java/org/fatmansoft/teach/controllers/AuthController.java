package org.fatmansoft.teach.controllers;

import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.fatmansoft.teach.models.*;
import org.fatmansoft.teach.payload.request.DataRequest;
import org.fatmansoft.teach.payload.response.DataResponse;
import org.fatmansoft.teach.repository.StudentRepository;
import org.fatmansoft.teach.repository.TeacherRepository;
import org.fatmansoft.teach.repository.UserTypeRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.fatmansoft.teach.payload.request.LoginRequest;
import org.fatmansoft.teach.payload.request.SignupRequest;
import org.fatmansoft.teach.payload.response.JwtResponse;
import org.fatmansoft.teach.payload.response.MessageResponse;
import org.fatmansoft.teach.repository.UserRepository;
import org.fatmansoft.teach.security.jwt.JwtUtils;
import org.fatmansoft.teach.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    UserTypeRepository userTypeRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        User user=userRepository.findByUserName(userDetails.getUsername()).get();
        user.setLastLoginTime(new Date());
        user.setLoginCount(user.getLoginCount()+1);
        userRepository.save(user);

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles.get(0)));
    }

    @PostMapping("/signup")
    public DataResponse registerUser(@Valid @RequestBody DataRequest signUpRequest) {
        if (userRepository.existsByUserName(signUpRequest.getString("username"))) {
            return /*ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"))*/
                    CommonMethod.getReturnMessageError("该用户名已被占用！");
        }

        // Create new user's account
        User user = new User(signUpRequest.getString("username"),
                encoder.encode(signUpRequest.getString("password")));

        String strRoles = signUpRequest.getString("role");


        if (strRoles == null) {
            UserType userRole = userTypeRepository.findByName(EUserType.ROLE_USER);
            user.setUserType(userRole);
        } else {
            UserType userRole = userTypeRepository.findByName(EUserType.valueOf(strRoles));
            user.setUserType(userRole);
        }
        switch (user.getUserType().getName()){
            case ROLE_USER:
                if(studentRepository.existsBySid(signUpRequest.getString("sid")))return CommonMethod.getReturnMessageError("该学号已被注册！");
                Student student=new Student();
                student.setName(signUpRequest.getString("name"));
                student.setSid(signUpRequest.getString("sid"));
                studentRepository.save(student);
                user.setStudent(student);
                break;
            case ROLE_ADMIN:
                if(teacherRepository.existsByTid(signUpRequest.getString("tid")))return CommonMethod.getReturnMessageError("该工号已被注册！");
                if(!signUpRequest.getString("check").equals("202022300310"))return CommonMethod.getReturnMessageError("校验密码错误！");
                Teacher teacher=new Teacher();
                teacher.setName(signUpRequest.getString("name"));
                teacher.setTid(signUpRequest.getString("tid"));
                teacherRepository.save(teacher);
                user.setTeacher(teacher);
                break;
        }


        userRepository.save(user);

        return CommonMethod.getReturnMessageOK();
    }


}
