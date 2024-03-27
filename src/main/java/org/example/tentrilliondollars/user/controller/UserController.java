package org.example.tentrilliondollars.user.controller;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.global.jwt.JwtUtil;
import org.example.tentrilliondollars.user.dto.LoginRequestDto;
import org.example.tentrilliondollars.user.dto.SignupRequestDto;
import org.example.tentrilliondollars.user.entity.User;
import org.example.tentrilliondollars.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;


    @PostMapping("/users/signup")
    public void signup(
        @Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
    }

    @PostMapping("/users/login")
    public void login(@RequestBody LoginRequestDto loginRequestDto,
        HttpServletResponse response) {
        User loginedUser = userService.login(loginRequestDto);

        String token = jwtUtil.createToken(loginedUser.getId(), loginedUser.getEmail(),
            loginedUser.getUsername(),
            loginedUser.getPassword(), loginedUser.getRole());

        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        jwtUtil.addJwtToCookie(token, response);
    }


}
