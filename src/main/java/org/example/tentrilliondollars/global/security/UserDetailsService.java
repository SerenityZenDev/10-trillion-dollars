package org.example.tentrilliondollars.global.security;

import lombok.RequiredArgsConstructor;
import org.example.tentrilliondollars.user.entity.User;
import org.example.tentrilliondollars.user.entity.UserRoleEnum;
import org.example.tentrilliondollars.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService {

    private final UserRepository userRepository;

    public UserDetails getUser(Long userId, String username, String email, String password,
        UserRoleEnum role)
        throws UsernameNotFoundException {
        User user = new User(userId, username, email, password, role);
        return new UserDetailsImpl(user);
    }
}
