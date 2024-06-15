package com.leultewolde.securitydemo.auth;

import com.leultewolde.securitydemo.user.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private Role role;
}
