package com.studentmgmt.config;

import com.studentmgmt.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * SECURITY CONFIGURATION
 * ========================
 * This class configures Spring Security for the application.
 *
 * WHAT IT CONTROLS:
 * - Which pages require login and which are public
 * - How login and logout work
 * - Password encryption method
 * - Role-based access control
 *
 * SECURITY FLOW:
 * ==============
 * 1. User accesses a protected page → Redirected to /login
 * 2. User submits login form → Spring Security authenticates
 * 3. If successful → Redirected to dashboard
 * 4. If failed → Redirected to /login?error
 * 5. User clicks logout → Session destroyed, redirected to /login?logout
 */
@Configuration       // Marks this as a configuration class
@EnableWebSecurity   // Enables Spring Security
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    /**
     * PASSWORD ENCODER BEAN
     * ======================
     * BCrypt is the industry standard for password hashing.
     * It adds a random "salt" to each password, making every hash unique
     * even for the same password.
     *
     * @Bean makes this available for dependency injection throughout the app.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AUTHENTICATION PROVIDER
     * ========================
     * Connects our CustomUserDetailsService (which loads users from DB)
     * with the password encoder (which verifies passwords).
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * AUTHENTICATION MANAGER
     * =======================
     * The central piece that manages the authentication process.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) 
            throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * SECURITY FILTER CHAIN
     * ======================
     * This is the MAIN security configuration.
     * It defines the rules for who can access what.
     *
     * RULE EXPLANATION:
     * - PUBLIC pages: /, /register, /login, /css/**, /js/**, /h2-console/**
     * - ADMIN-only pages: /students/delete/**
     * - ALL OTHER pages: Require authentication (login)
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Configure authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public pages - anyone can access (even without login)
                .requestMatchers(
                    "/",
                    "/register/**",
                    "/login/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/h2-console/**"
                ).permitAll()

                // Only ADMIN can delete students
                .requestMatchers("/students/delete/**").hasRole("ADMIN")

                // Everything else requires login
                .anyRequest().authenticated()
            )

            // Configure login page
            .formLogin(form -> form
                .loginPage("/login")                      // Custom login page URL
                .loginProcessingUrl("/login")             // Form action URL
                .usernameParameter("email")               // We use email instead of username
                .defaultSuccessUrl("/dashboard", true)    // Redirect after successful login
                .failureUrl("/login?error=true")          // Redirect after failed login
                .permitAll()
            )

            // Configure logout
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true")   // Redirect after logout
                .invalidateHttpSession(true)              // Destroy session
                .deleteCookies("JSESSIONID")              // Delete session cookie
                .permitAll()
            )

            // Disable CSRF for H2 console (development only!)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )

            // Allow H2 console to be displayed in frames
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }
}
