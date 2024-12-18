package com.sevar.ecommerce.controller;

import com.sevar.ecommerce.model.Users;
import com.sevar.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<Users> getUserProfile(@RequestHeader("Authorization") String jwt){
        Users users = userService.findUserProfileByJwt(jwt);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Users>> getAllUsers(){
        List<Users> list = userService.getAllUsers();
        return new ResponseEntity<>(list,HttpStatus.ACCEPTED);
    }

}
