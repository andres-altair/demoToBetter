package com.andres.demotobetter.modules.security.service;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.andres.demotobetter.common.exception.custom.BadRequestException;
import com.andres.demotobetter.modules.security.entity.UserSecurity;
import com.andres.demotobetter.modules.security.repository.UserSecurityRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserSecurityServiceImpl implements UserSecurityService {
    private static final String ERR_BAD_REQUEST = "ERR_BAD_REQUEST"; 
    private final UserSecurityRepository userSecurityRepository; 
    private final RoleService roleService; 
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserSecurity createSecurityUser(String email, String password, Set<String> roles) {
        if(userSecurityRepository.findByEmail(email).isPresent()){
            throw new BadRequestException(ERR_BAD_REQUEST, "Email already in use");
        }
        UserSecurity userSecurity = new UserSecurity();
        userSecurity.setUsername(email);
        userSecurity.setEmail(email);
        userSecurity.setPassword(passwordEncoder.encode(password));
        userSecurity.setRoles(roleService.resolveRoles(roles));
        return userSecurityRepository.save(userSecurity);
    }
}
