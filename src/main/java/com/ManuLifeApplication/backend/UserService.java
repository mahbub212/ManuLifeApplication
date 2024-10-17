package com.ManuLifeApplication.backend;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UsersRepository usersRepository;

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<User> getAllUser() {
        return usersRepository.findAll();
    }

}
