package com.example.employee.dto;

import com.example.employee.dto.AuthRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthRequestTest {

    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        authRequest = new AuthRequest();
    }

    @Test
    void testAuthRequestCreation() {
        assertThat(authRequest).isNotNull();
        assertThat(authRequest.getUsername()).isNull();
        assertThat(authRequest.getPassword()).isNull();
        assertThat(authRequest.getRole()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        authRequest.setUsername("testuser");
        authRequest.setPassword("testpassword");
        authRequest.setRole("ROLE_USER");

        assertThat(authRequest.getUsername()).isEqualTo("testuser");
        assertThat(authRequest.getPassword()).isEqualTo("testpassword");
        assertThat(authRequest.getRole()).isEqualTo("ROLE_USER");
    }

    @Test
    void testAuthRequestWithNullValues() {
        authRequest.setUsername(null);
        authRequest.setPassword(null);
        authRequest.setRole(null);

        assertThat(authRequest.getUsername()).isNull();
        assertThat(authRequest.getPassword()).isNull();
        assertThat(authRequest.getRole()).isNull();
    }

    @Test
    void testAuthRequestWithEmptyValues() {
        authRequest.setUsername("");
        authRequest.setPassword("");
        authRequest.setRole("");

        assertThat(authRequest.getUsername()).isEqualTo("");
        assertThat(authRequest.getPassword()).isEqualTo("");
        assertThat(authRequest.getRole()).isEqualTo("");
    }

    @Test
    void testAuthRequestWithAdminRole() {
        authRequest.setUsername("admin");
        authRequest.setPassword("adminpass");
        authRequest.setRole("ROLE_ADMIN");

        assertThat(authRequest.getUsername()).isEqualTo("admin");
        assertThat(authRequest.getPassword()).isEqualTo("adminpass");
        assertThat(authRequest.getRole()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void testAuthRequestWithoutRole() {
        authRequest.setUsername("user");
        authRequest.setPassword("userpass");
        // Don't set role - should remain null

        assertThat(authRequest.getUsername()).isEqualTo("user");
        assertThat(authRequest.getPassword()).isEqualTo("userpass");
        assertThat(authRequest.getRole()).isNull();
    }

    @Test
    void testAuthRequestWithSpecialCharacters() {
        authRequest.setUsername("user@example.com");
        authRequest.setPassword("p@ssw0rd!#$");
        authRequest.setRole("ROLE_USER");

        assertThat(authRequest.getUsername()).isEqualTo("user@example.com");
        assertThat(authRequest.getPassword()).isEqualTo("p@ssw0rd!#$");
        assertThat(authRequest.getRole()).isEqualTo("ROLE_USER");
    }

    @Test
    void testAuthRequestFieldModification() {
        authRequest.setUsername("initial");
        assertThat(authRequest.getUsername()).isEqualTo("initial");

        authRequest.setUsername("modified");
        assertThat(authRequest.getUsername()).isEqualTo("modified");
    }
}