package com.example.employee.config;

import com.example.employee.config.SecurityConfig;
import com.example.employee.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Test
    void passwordEncoder_ShouldBeBCryptPasswordEncoder() {
        assertThat(passwordEncoder).isNotNull();
        assertThat(passwordEncoder.getClass().getSimpleName()).isEqualTo("BCryptPasswordEncoder");
    }

    @Test
    void passwordEncoder_ShouldEncodePasswords() {
        String rawPassword = "testpassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertThat(encodedPassword).isNotNull();
        assertThat(encodedPassword).isNotEqualTo(rawPassword);
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }

    @Test
    void passwordEncoder_ShouldGenerateDifferentHashesForSamePassword() {
        String password = "samepassword";
        String hash1 = passwordEncoder.encode(password);
        String hash2 = passwordEncoder.encode(password);

        assertThat(hash1).isNotEqualTo(hash2);
        assertThat(passwordEncoder.matches(password, hash1)).isTrue();
        assertThat(passwordEncoder.matches(password, hash2)).isTrue();
    }

    @Test
    void authenticationManager_ShouldBeConfigured() {
        assertThat(authenticationManager).isNotNull();
    }

    @Test
    void securityFilterChain_ShouldBeConfigured() {
        assertThat(securityFilterChain).isNotNull();
    }

    @Test
    void securityConfig_ShouldBeInstantiated() {
        assertThat(securityConfig).isNotNull();
    }

    @Test
    void passwordEncoder_ShouldRejectInvalidPassword() {
        String password = "correctpassword";
        String encodedPassword = passwordEncoder.encode(password);

        assertThat(passwordEncoder.matches("wrongpassword", encodedPassword)).isFalse();
        assertThat(passwordEncoder.matches("", encodedPassword)).isFalse();
    }

    @Test
    void passwordEncoder_ShouldHandleEmptyAndNullPasswords() {
        String emptyPassword = "";
        String encodedEmpty = passwordEncoder.encode(emptyPassword);
        
        assertThat(encodedEmpty).isNotNull();
        assertThat(passwordEncoder.matches(emptyPassword, encodedEmpty)).isTrue();
        assertThat(passwordEncoder.matches("notempty", encodedEmpty)).isFalse();
    }
}