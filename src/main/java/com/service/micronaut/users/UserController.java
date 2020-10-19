package com.service.micronaut.users;

import io.micronaut.http.annotation.*;

import java.util.List;

@Controller("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Get
    public List<UserData> getAllUsers() {
        return userRepository.findAll();
    }

    @Get("/{user_id}")
    public UserData getUserById(@PathVariable("user_id") Long userId) {
        return userRepository.findById(userId);
    }

    @Post
    public UserData saveUser(@Body UserData userData) {
        return userRepository.save(userData);
    }

    @Delete("/{user_id}")
    public void deleteUserById(@PathVariable("user_id") Long userId) {
        userRepository.deleteById(userId);
    }
}
