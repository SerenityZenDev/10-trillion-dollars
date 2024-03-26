package org.example.tentrilliondollars.user.repository;

import org.example.tentrilliondollars.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
