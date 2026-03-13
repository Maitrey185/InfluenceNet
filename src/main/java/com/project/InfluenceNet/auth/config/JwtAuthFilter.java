package com.project.InfluenceNet.auth.config;

import com.project.InfluenceNet.auth.dto.JwtAuthToken;
import com.project.InfluenceNet.auth.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        log.trace("JwtAuthFilter skipping request path={}", path);
        return true;
//        return path.startsWith("/auth")
//                || path.startsWith("/v3/api-docs")
//                || path.startsWith("/swagger-ui")
//                || path.startsWith("/swagger-resources")
//                || path.startsWith("/webjars")
//                || path.equals("/swagger-ui.html");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = extractTokenFromRequest(request);

        if (token != null) {
            try {
                log.debug("JwtAuthFilter found bearer token for path={}", request.getServletPath());
                JwtAuthToken authToken = new JwtAuthToken(token);
                Authentication authentication = authenticationManager.authenticate(authToken);
                if (authentication.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("JwtAuthFilter set SecurityContext for path={}", request.getServletPath());
                } else {
                    log.debug("JwtAuthFilter authentication manager returned non-authenticated for path={}", request.getServletPath());
                }
            } catch (Exception ex) {
                log.warn("JwtAuthFilter failed to authenticate request path={} reason={}", request.getServletPath(), ex.getClass().getSimpleName());
            }
        } else {
            log.trace("JwtAuthFilter no bearer token for path={}", request.getServletPath());
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
