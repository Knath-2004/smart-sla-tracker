package com.smartsla.smart_sla_tracker.config;

import com.smartsla.smart_sla_tracker.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {}) // IMPORTANT for frontend

                // JWT = Stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        // 🔓 PUBLIC
                        .requestMatchers("/auth/**").permitAll()

                        // 👤 USER + ADMIN can access tickets
                        .requestMatchers("/tickets/**")
                        .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

                        // 🛠️ ADMIN only
                        .requestMatchers("/admin/**")
                        .hasAuthority("ROLE_ADMIN")

                        // 🧑‍💻 AGENT only
                        .requestMatchers("/agent/**")
                        .hasAuthority("ROLE_AGENT")

                        // 🔒 everything else
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


