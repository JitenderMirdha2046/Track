package com.pm.track.user.service;


import com.pm.track.auth.dto.SignupRequest;
import com.pm.track.user.dto.ChangePasswordRequest;
import com.pm.track.auth.dto.LoginRequest;
import com.pm.track.user.dto.UserRequest;
import com.pm.track.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse registerUser(SignupRequest request);
    String loginUser(LoginRequest request);
    UserResponse getCurrentUser();
    UserResponse updateCurrentUser(UserRequest userRequest);
    List<UserResponse> getAllUsers();

    void changePassword(ChangePasswordRequest request);
}
