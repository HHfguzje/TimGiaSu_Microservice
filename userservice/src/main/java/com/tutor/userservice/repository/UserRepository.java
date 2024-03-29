package com.tutor.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tutor.userservice.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUsername(String username);
	Optional<User> findByVerificationToken(String token);
	Optional<User> findByEmail(String email);
	Optional<User> findByverificationToken(String verificationToken);
}
