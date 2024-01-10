package com.tutor.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tutor.userservice.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
