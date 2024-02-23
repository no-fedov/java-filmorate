package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUser();
    }

    @PostMapping
    public User postUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User putUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User getUserByID(@PathVariable Integer id) {
        return userService.findUser(id);
    }

    @DeleteMapping("/{id}")
    public User delUserByID(@PathVariable Integer id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer id1, @PathVariable("friendId") Integer id2) {
        userService.manageFriendship(id1, id2, UserService.FriendshipMethod.ADD);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer id1, @PathVariable("friendId") Integer id2) {
        userService.manageFriendship(id1, id2, UserService.FriendshipMethod.DEL);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriend(@PathVariable Integer id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable("id") Integer id1, @PathVariable("otherId") Integer id2) {
        return userService.getMutualFriends(id1, id2);
    }
}