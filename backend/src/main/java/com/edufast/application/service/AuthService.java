package com.edufast.application.service;

import com.edufast.application.dto.LoginRequest;
import com.edufast.application.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}