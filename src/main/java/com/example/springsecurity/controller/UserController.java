package com.example.springsecurity.controller;

import com.example.springsecurity.exception.EmailAlreadyExistsException;
import com.example.springsecurity.exception.ResourceNotFoundException;
import com.example.springsecurity.model.User;
import com.example.springsecurity.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private AuthService authService;

    @PostMapping("/public/signup")  /** public endpoint for signups */
    public ResponseEntity<Object> signup(@Valid @RequestBody User user) throws EmailAlreadyExistsException {
        return new ResponseEntity<>(authService.signUp(user), HttpStatus.CREATED);
    }

    @PostMapping("/public/signin") /** public endpoint for signing in */
    public ResponseEntity<Object> signin(@RequestBody User user) {
        return new ResponseEntity<>(authService.signIn(user), HttpStatus.OK);
    }

    @PutMapping("/user/update")     /** user-authenticated endpoint for updating user profile */
    public ResponseEntity<Object> update(
            @RequestParam("data") String data, // "{'userName': 'JohnDoe', 'email': "jd@gmail.com'}"
            @Nullable @RequestParam(value = "image", required = false) MultipartFile image
            ) throws IOException, ResourceNotFoundException {

        // Convert "data" stored as a string into User object
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(data, User.class);

        return new ResponseEntity<>(authService.update(user, image), HttpStatus.OK);
    }

}
