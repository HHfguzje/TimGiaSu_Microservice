package com.tutor.userservice.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.tutor.userservice.repository.UserRepository;
import com.tutor.userservice.service.UserService;

public class UserServiceImpl implements UserService{
	@Autowired
	UserRepository userRepository;
}
