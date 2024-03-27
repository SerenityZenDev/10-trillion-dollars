package org.example.tentrilliondollars.user.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.user.dto.LoginRequestDto;
import org.example.tentrilliondollars.user.dto.SignupRequestDto;
import org.example.tentrilliondollars.user.entity.User;
import org.example.tentrilliondollars.user.entity.UserRoleEnum;
import org.example.tentrilliondollars.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    private final String SELLER_TOKEN = "AAABnvxRVklrnYxKZaHgTBcXukeZygoC";


    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String email = signupRequestDto.getEmail();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 username입니다..");
        }

        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 email입니다.");
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if (signupRequestDto.isAdmin()) {
            if (!SELLER_TOKEN.equals(signupRequestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 일치하지 않습니다.");
            }
            role = UserRoleEnum.SELLER;
        }

        userRepository.save(User.builder()
            .email(email)
            .username(username)
            .password(password)
            .role(role)
            .build());
    }

    public void login(LoginRequestDto loginRequestDto) {

        User user = userRepository.findByEmail(loginRequestDto.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }


    }
}
