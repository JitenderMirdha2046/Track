package com.pm.track.auth.controller;
import com.pm.track.auth.dto.ForgotPasswordRequest;
import com.pm.track.auth.dto.LoginRequest;
import com.pm.track.auth.dto.ResetPasswordRequest;
import com.pm.track.auth.dto.SignupRequest;
import com.pm.track.auth.service.PasswordResetService;
import com.pm.track.user.service.UserService;
import com.pm.track.user.dto.UserResponse;
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
    private final PasswordResetService passwordResetService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody SignupRequest request) {
        return new ResponseEntity<>(userService.registerUser(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@Valid @RequestBody LoginRequest request) {
        String jwtToken = userService.loginUser(request);
        return ResponseEntity.ok(Map.of("token", jwtToken));
    }



    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.createPasswordResetToken(request.getEmail());
        return ResponseEntity.ok("Reset token sent (check logs or email service)");
    }


    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password updated successfully");
    }
}
