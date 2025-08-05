package com.deliverytech.delivery.service;

import com.deliverytech.delivery.dto.request.LoginRequest;
import com.deliverytech.delivery.dto.request.RegisterRequest;
import com.deliverytech.delivery.dto.response.LoginResponse;
import com.deliverytech.delivery.dto.response.UserResponse;
import com.deliverytech.delivery.model.Usuario;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    Usuario register(RegisterRequest request);

    UserResponse getCurrentUser();
}