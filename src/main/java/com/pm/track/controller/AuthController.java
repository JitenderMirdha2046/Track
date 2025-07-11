package com.pm.track.controller;
import com.pm.track.dto.LoginRequest;
import com.pm.track.dto.SignupRequest;
import com.pm.track.dto.UserResponse;
import com.pm.track.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody SignupRequest request) {
        return new ResponseEntity<>(userService.registerUser(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@Valid @RequestBody LoginRequest request) {
        String jwtToken = userService.loginUser(request);
        return ResponseEntity.ok(Map.of("token", jwtToken));
    }
}
