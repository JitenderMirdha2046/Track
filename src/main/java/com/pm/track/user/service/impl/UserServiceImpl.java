package com.pm.track.user.service.impl;

import com.pm.track.auth.dto.SignupRequest;
import com.pm.track.user.dto.ChangePasswordRequest;
import com.pm.track.auth.dto.LoginRequest;
import com.pm.track.user.entity.User;
import com.pm.track.common.enums.UserRole;
import com.pm.track.exception.CustomException;
import com.pm.track.user.repository.UserRepository;
import com.pm.track.auth.security.JwtUtil;
import com.pm.track.auth.security.UserDetailsImpl;
import com.pm.track.common.EmailService;
import com.pm.track.user.service.UserService;
import com.pm.track.user.dto.UserRequest;
import com.pm.track.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponse registerUser(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException("Email already exists", HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : UserRole.USER)
                .enabled(true)
                .build();

        userRepository.save(user);
        emailService.sendWelcomeEmail(user.getEmail(), user.getName());

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public String loginUser(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetailsImpl) authentication.getPrincipal();
        emailService.sendReturningUserOfferEmail(userDetails.getUsername(), userDetails.getPassword());
        return jwtUtil.generateToken(userDetails.getUsername());
    }


    @Override
    public UserResponse getCurrentUser() {
        String email = getCurrentEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        return mapToResponse(user);
    }

    @Override
    public UserResponse updateCurrentUser(UserRequest request) {
        String email = getCurrentEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        user.setName(request.getName());
        user.setEnabled(request.isEnabled());

        userRepository.save(user);

        return mapToResponse(user);
    }

    private String getCurrentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName(); // email
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }


    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public void changePassword(ChangePasswordRequest request) {
        String email = getCurrentEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        // Check if old password matches
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new CustomException("Old password is incorrect", HttpStatus.BAD_REQUEST);
        }

        // Check if new password is same as old
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new CustomException("New password cannot be same as old password", HttpStatus.BAD_REQUEST);
        }

        // Set and save new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }


}

