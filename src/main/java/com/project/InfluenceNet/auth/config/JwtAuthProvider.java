package com.project.InfluenceNet.auth.config;

import com.project.InfluenceNet.auth.dto.JwtAuthToken;
import com.project.InfluenceNet.auth.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getName();
        log.debug("JWT authentication attempt");
        String username = jwtUtil.extractUsername(token);
        if(username==null){
            log.warn("JWT authentication failed: username could not be extracted");
            throw new RuntimeException("Invalid token");
        }
        log.debug("JWT authentication resolved username={}", username);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        log.debug("JWT authentication succeeded for username={}", username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthToken.class.isAssignableFrom(authentication);
    }
}
