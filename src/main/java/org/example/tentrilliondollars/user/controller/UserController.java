package org.example.tentrilliondollars.user.controller;


import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.global.jwt.JwtUtil;
import org.example.tentrilliondollars.global.security.UserDetailsImpl;
import org.example.tentrilliondollars.user.dto.LoginRequestDto;
import org.example.tentrilliondollars.user.dto.ModifyPasswordRequestDto;
import org.example.tentrilliondollars.user.dto.ModifyUserNameRequestDto;
import org.example.tentrilliondollars.user.dto.SignupRequestDto;
import org.example.tentrilliondollars.user.entity.User;
import org.example.tentrilliondollars.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
            loginedUser.getUsername(), loginedUser.getRole());

        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        jwtUtil.addJwtToCookie(token, response);
    }

    @PutMapping("/users/username")
    public void modifyUsername(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody
        ModifyUserNameRequestDto requestDto) {
        userService.modifyUsername(userDetails.getUser(), requestDto);
    }

    @PutMapping("/users/password")
    public void modifyPassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody
        ModifyPasswordRequestDto requestDto) {
        userService.modifyPassword(userDetails.getUser(), requestDto);
    }


}
