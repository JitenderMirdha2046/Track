package com.pm.track.service;


import com.pm.track.dto.LoginRequest;
import com.pm.track.dto.SignupRequest;
import com.pm.track.dto.UserRequest;
import com.pm.track.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse registerUser(SignupRequest request);
    String loginUser(LoginRequest request);
    UserResponse getCurrentUser();
    UserResponse updateCurrentUser(UserRequest userRequest);
    List<UserResponse> getAllUsers();

}
