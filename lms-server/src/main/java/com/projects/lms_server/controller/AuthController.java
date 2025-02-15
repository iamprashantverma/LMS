package com.projects.lms_server.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.projects.lms_server.dto.LoginReqDTO;
import com.projects.lms_server.dto.LoginResDTO;
import com.projects.lms_server.dto.OtpDTO;
import com.projects.lms_server.dto.UserDTO;
import com.projects.lms_server.service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.parser.Parser;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    /* injecting the userService Dependency */
    private final UserService userService;
    private final Cloudinary cloudinary;

    public AuthController(UserService userService, Cloudinary cloudinary) {
        this.userService = userService;
        this.cloudinary = cloudinary;
    }

    /* Initiating Member Enrollment Process */
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@Valid @RequestBody UserDTO user) {
        UserDTO regResp = userService.signUp(user);
        return  ResponseEntity.ok(regResp);
    }

    /* Log in*/
    @PostMapping("/login")
    public ResponseEntity<LoginResDTO> login(@RequestBody LoginReqDTO cred) {
            LoginResDTO login = userService.login(cred) ;
            return ResponseEntity.ok(login) ;
    }

    /* send otp to user email*/
    @GetMapping("/otp")
    public ResponseEntity<OtpDTO> sendOTP(HttpServletRequest req) {
//        OtpDTO otp = userService.sendOtp(otpDTO)
        return ResponseEntity.ok(new OtpDTO());
    }





}
