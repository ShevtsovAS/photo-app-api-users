package com.example.photoapp.api.users.security;

import com.example.photoapp.api.users.service.UserService;
import com.example.photoapp.api.users.ui.model.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static com.example.photoapp.api.users.utils.DateTimeUtil.getDate;
import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final UserService userService;
    private final Environment environment;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword(),
                            Collections.emptyList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {

        String userId = userService.getUserDetailsByEmail(((User) authResult.getPrincipal()).getUsername()).getUserId();

        long expirationTime = Optional.ofNullable(environment.getProperty("token.expiration-time"))
                .map(Long::parseLong)
                .orElse(24L);

        String token = Jwts.builder()
                .setSubject(userId)
                .setExpiration(getDate(now().plusHours(expirationTime)))
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
                .compact();

        response.addHeader("token", token);
        response.addHeader("userId", userId);
    }
}
