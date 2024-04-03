package org.example.tentrilliondollars.user.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.user.dto.DeleteUserRequestDto;
import org.example.tentrilliondollars.user.dto.LoginRequestDto;
import org.example.tentrilliondollars.user.dto.ModifyPasswordRequestDto;
import org.example.tentrilliondollars.user.dto.ModifyUserNameRequestDto;
import org.example.tentrilliondollars.user.dto.SignupRequestDto;
import org.example.tentrilliondollars.user.entity.User;
import org.example.tentrilliondollars.user.entity.UserRoleEnum;
import org.example.tentrilliondollars.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public User login(LoginRequestDto loginRequestDto) {

        User user = userRepository.findByEmail(loginRequestDto.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 계정입니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    @Transactional
    public void modifyUsername(User user, ModifyUserNameRequestDto modifyUserNameRequestDto) {
        User changeNameUser = userRepository.findById(user.getId()).orElseThrow();

        changeNameUser.modifyUsername(modifyUserNameRequestDto.getUsername());
    }

    @Transactional
    public void modifyPassword(User user, ModifyPasswordRequestDto modifyPasswordRequestDto) {
        User changePasswordUser = userRepository.findById(user.getId()).orElseThrow();

        if (!passwordEncoder.matches(modifyPasswordRequestDto.getPassword(),
            changePasswordUser.getPassword())) {
            throw new IllegalArgumentException("비밀번호 불일치");
        }

        if (!modifyPasswordRequestDto.getChangePassword()
            .equals(modifyPasswordRequestDto.getChangePasswordCheck())) {
            throw new IllegalArgumentException("변경할 비밀번호 확인");
        }

        String changedPassword = passwordEncoder.encode(
            modifyPasswordRequestDto.getChangePasswordCheck());

        changePasswordUser.modifyPassword(changedPassword);
    }

    public void deleteUser(User user, DeleteUserRequestDto deleteUserRequestDto) {
        User deleteUser = userRepository.findById(user.getId()).orElseThrow();

        if (!deleteUserRequestDto.getPassword().equals(deleteUserRequestDto.getPasswordCheck())) {
            throw new IllegalArgumentException("비밀번호");
        }

        if (!passwordEncoder.matches(deleteUserRequestDto.getPassword(),
            deleteUser.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        userRepository.deleteById(user.getId());
    }
    //테스트
    public void createMultipleUsers() {
        for (int i = 4; i <= 100; i++) {
            String username = "user" + i;
            String email = "user" + i + "@example.com";
            String password = "Test1234!"; // 예시 비밀번호, 실제로는 각 사용자별로 고유한 비밀번호를 설정해야 함

            // 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(password);

            // 사용자 역할 설정 (예시에서는 모든 사용자를 일반 사용자로 설정)
            UserRoleEnum role = UserRoleEnum.USER;

            // 사용자 생성 및 저장
            userRepository.save(User.builder()
                .email(email)
                .username(username)
                .password(encodedPassword)
                .role(role)
                .build());
        }
    }


}
