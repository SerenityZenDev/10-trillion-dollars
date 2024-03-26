package org.example.tentrilliondollars.user.controller;


import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.user.service.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

}
