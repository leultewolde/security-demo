package com.leultewolde.securitydemo.auth;

import com.leultewolde.securitydemo.config.JwtService;
import com.leultewolde.securitydemo.user.User;
import com.leultewolde.securitydemo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        // Create a user
        User user = new User(
                registerRequest.getFirstname(),
                registerRequest.getLastname(),
                registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getRole()
        );
        // Save user
        User savedUser = userRepository.save(user);
        //Generate a token
        String token = jwtService.generateToken(savedUser);

        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

//        var user = userRepository.findUserByUsername(request.getUsername())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var user = userDetailsService.loadUserByUsername(request.getUsername());

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }

}
