package com.cursus.userservice.service;

import com.cursus.userservice.dto.UserDTO;

public interface SettingService {
    UserDTO viewProfile(Long id);

    UserDTO editProfile(UserDTO user);

    UserDTO changePassword(UserDTO user);
}
