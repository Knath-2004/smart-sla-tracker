package com.smartsla.smart_sla_tracker.security;



import com.smartsla.smart_sla_tracker.entity.AppUser;
import com.smartsla.smart_sla_tracker.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);
            String username = jwtUtil.extractUsername(token);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                AppUser user =
                        userRepository.findByUsername(username).orElse(null);

                if (user != null) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    user.getUsername(),
                                    null,
                                    List.of(
                                            new SimpleGrantedAuthority(
                                                    "ROLE_" + user.getRole().name()
                                            )
                                    )
                            );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

