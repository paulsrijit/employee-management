package com.example.employee.dto;

import com.example.employee.dto.AuthResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthResponseTest {

    @Test
    void testDefaultConstructor() {
        AuthResponse response = new AuthResponse();
        
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isNull();
    }

    @Test
    void testParameterizedConstructor() {
        String token = "jwt-token-123";
        AuthResponse response = new AuthResponse(token);
        
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(token);
    }

    @Test
    void testSettersAndGetters() {
        AuthResponse response = new AuthResponse();
        String token = "test-jwt-token";
        
        response.setToken(token);
        
        assertThat(response.getToken()).isEqualTo(token);
    }

    @Test
    void testTokenWithNullValue() {
        AuthResponse response = new AuthResponse(null);
        
        assertThat(response.getToken()).isNull();
        
        response.setToken(null);
        assertThat(response.getToken()).isNull();
    }

    @Test
    void testTokenWithEmptyValue() {
        AuthResponse response = new AuthResponse("");
        
        assertThat(response.getToken()).isEqualTo("");
        
        response.setToken("");
        assertThat(response.getToken()).isEqualTo("");
    }

    @Test
    void testTokenWithLongJwtValue() {
        String longJwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ." +
                "SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        
        AuthResponse response = new AuthResponse(longJwtToken);
        
        assertThat(response.getToken()).isEqualTo(longJwtToken);
    }

    @Test
    void testTokenModification() {
        AuthResponse response = new AuthResponse("initial-token");
        
        assertThat(response.getToken()).isEqualTo("initial-token");
        
        response.setToken("modified-token");
        assertThat(response.getToken()).isEqualTo("modified-token");
    }

    @Test
    void testMultipleInstances() {
        AuthResponse response1 = new AuthResponse("token1");
        AuthResponse response2 = new AuthResponse("token2");
        
        assertThat(response1.getToken()).isEqualTo("token1");
        assertThat(response2.getToken()).isEqualTo("token2");
        assertThat(response1.getToken()).isNotEqualTo(response2.getToken());
    }
}