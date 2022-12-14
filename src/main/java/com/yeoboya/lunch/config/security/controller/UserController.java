package com.yeoboya.lunch.config.security.controller;

import com.yeoboya.lunch.api.v1.common.response.Response.Body;
import com.yeoboya.lunch.config.annotation.TimeLogging;
import com.yeoboya.lunch.config.security.reqeust.UserRequest.*;
import com.yeoboya.lunch.config.security.service.UserService;
import com.yeoboya.lunch.config.security.validation.ValidationGroups.KnowOldPassword;
import com.yeoboya.lunch.config.security.validation.ValidationGroups.UnKnowOldPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SignUpFormValidator signUpFormValidator;

//    @InitBinder(value = "signUp")
//    public void initBinder(WebDataBinder webDataBinder) {
//        webDataBinder.addValidators(signUpFormValidator);
//    }

    /**
     * 회원가입
     */
    @PostMapping("/sign-up")
    public ResponseEntity<Body> signUp(@Valid @RequestBody SignUp signUp) {
        return userService.signUp(signUp);
    }

    /**
     * 로그인
     */
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignIn signIn) {
        return userService.signIn(signIn);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/sign-out")
    public ResponseEntity<Body> signOut(@Valid @RequestBody SignOut signOut) {
        return userService.signOut(signOut);
    }

    /**
     * 비밀번호 변경
     */
    @PatchMapping("/setting/security")
    public ResponseEntity<Body> changePassword(@Validated(KnowOldPassword.class) @RequestBody Credentials credentials){
        return userService.changePassword(credentials);
    }

    /**
     * 비밀번호 초기화
     */
    @PatchMapping("/resetPassword")
    public ResponseEntity<Body> resetPassword(@Validated(UnKnowOldPassword.class) @RequestBody Credentials credentials){
        return userService.resetPassword(credentials);
    }

    /**
     * 토큰 재발급
     */
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@Valid @RequestBody Reissue reissue) {
        return userService.reIssue(reissue);
    }

    /**
     * 권한추가
     */
    @GetMapping("/authority")
    public ResponseEntity<?> authority(HttpServletRequest request) {
        return userService.authority(request);
    }


    /**
     * 비밀번호 변경 이메일 전송
     */
    @TimeLogging
    @GetMapping("/sendResetPasswordMail/{memberEmail}")
    public ResponseEntity<Body> sendResetPasswordMail(@PathVariable String memberEmail) {
        return userService.sendResetPasswordMail(memberEmail);
    }
}