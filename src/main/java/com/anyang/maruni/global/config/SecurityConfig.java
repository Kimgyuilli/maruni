package com.anyang.maruni.global.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String[] PUBLIC_URLS = {
            "/api/join", "/api/join/email-check", "/api/auth/login",
            "/api/auth/token/refresh", "/api/auth/token/refresh/full", "/api/auth/logout", "/api-docs", "/v3/api-docs/**", "/swagger-ui/**",
    };

    private static final String[] ADMIN_URLS = {
            "/api/admin/**"
    };

//    private final JWTUtil jwtUtil;
//    private final ObjectMapper objectMapper;
//    private final RefreshTokenService refreshTokenService;
//    private final BlacklistService blacklistService;
//    private final CustomUserDetailsService customUserDetailsService;
//    private final JWTService jwtService;

//    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
//    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
//    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures and returns the application's security filter chain.
     *
     * Disables CSRF protection, form login, and HTTP Basic authentication. Configures CORS to allow all origins, methods, and headers, and exposes the "Authorization" header. Sets session management to stateless and permits all HTTP requests without authentication. Responds with HTTP 401 Unauthorized for authentication failures.
     *
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during security configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {

//        LoginFilter loginFilter = new LoginFilter(authManager, objectMapper, jwtService);
//        loginFilter.setFilterProcessesUrl("/api/auth/login");

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOriginPatterns(List.of("*"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(List.of("*"));
                    config.setExposedHeaders(List.of("Authorization"));
                    config.setMaxAge(3600L);
                    return config;
                }))

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())

                .exceptionHandling(except -> except
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")));


//        http.oauth2Login(oauth2 -> oauth2
//                .successHandler(oAuth2LoginSuccessHandler)
//                .failureHandler(oAuth2LoginFailureHandler)
//                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
//        );




//        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(
//                new JwtAuthenticationFilter(jwtUtil, blacklistService, customUserDetailsService),
//                UsernamePasswordAuthenticationFilter.class
//        );

        return http.build();
    }
}
