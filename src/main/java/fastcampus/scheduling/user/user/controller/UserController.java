package fastcampus.scheduling.user.user.controller;

import fastcampus.scheduling.user.user.dto.UserRequest;
import fastcampus.scheduling.user.user.dto.UserResponse.SignUpDTO;
import fastcampus.scheduling.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @GetMapping("/v1/auth/signup")
    public String singUp(){
        log.info("/v1/auth/signup GET");
        return "/signup"; //임시-회원가입 폼
    }

    @PostMapping("/v1/auth/signup")
    public String signUp(@RequestBody UserRequest.SignUpDTO signUpDTO, Errors errors) { //요청으로 토큰을 받아서 
        log.info("/v1/auth/signup POST " + signUpDTO);
        SignUpDTO signUpResponse = userService.save(signUpDTO);

        //로그인 메서드 호출

        return "redirect:/main"; //임시-메인 폼
    }

    @PostMapping("v1/auth/signup/cancel")
    public String signUpCancel(){
        log.info("/v1/auth/signup/cancel POST");
        return "/signin"; //임시-로그인 폼
    }
}
