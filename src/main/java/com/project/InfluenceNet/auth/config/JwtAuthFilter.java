package com.project.InfluenceNet.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.InfluenceNet.auth.dto.LoginRequest;
import com.project.InfluenceNet.auth.dto.RegisterRequest;
import com.project.InfluenceNet.auth.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        // Skip JWT validation for register and login endpoints
        return path.startsWith("/auth") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


//        ObjectMapper objectMapper = new ObjectMapper();
//        LoginRequest loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
//
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                loginRequest.getUsername(), loginRequest.getPassword()
//        );
//
//        Authentication auth = authenticationManager.authenticate(authenticationToken);
//
//        if(auth.isAuthenticated()){
//            String token = jwtUtil.generateToken(loginRequest.getUsername(), 15);
//            response.addHeader("Authorization", "Bearer " + token);
//        }
        filterChain.doFilter(request, response);
    }
}
