package com.leultewolde.securitydemo.config;

import com.leultewolde.securitydemo.user.Permission;
import com.leultewolde.securitydemo.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(req -> req
                        .requestMatchers("api/v1/auth/*")
                        .permitAll()
                        .requestMatchers("/api/v1/management/**")
                        .hasAnyRole(Role.ADMIN.name(), Role.MEMBER.name())
                        .requestMatchers("/api/v1/management/foradmin").hasAuthority(Permission.ADMIN_WRITE.getPermission())
                        .requestMatchers("/api/v1/management/adminwrite").hasAuthority(Permission.ADMIN_WRITE.getPermission())
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
