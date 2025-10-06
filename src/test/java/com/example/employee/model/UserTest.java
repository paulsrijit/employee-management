package com.example.employee.model;

import com.example.employee.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testUserCreation() {
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNull();
        assertThat(user.getUsername()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.getRoles()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRoles(Set.of("ROLE_USER"));

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("testuser");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getRoles()).containsExactly("ROLE_USER");
    }

    @Test
    void testUserWithMultipleRoles() {
        Set<String> roles = Set.of("ROLE_USER", "ROLE_ADMIN");
        user.setRoles(roles);

        assertThat(user.getRoles()).hasSize(2);
        assertThat(user.getRoles()).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }

    @Test
    void testUserWithEmptyRoles() {
        user.setRoles(Set.of());
        assertThat(user.getRoles()).isEmpty();
    }

    @Test
    void testUserWithNullValues() {
        user.setId(null);
        user.setUsername(null);
        user.setPassword(null);
        user.setRoles(null);

        assertThat(user.getId()).isNull();
        assertThat(user.getUsername()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.getRoles()).isNull();
    }

    @Test
    void testUserWithCompleteData() {
        user.setId(123L);
        user.setUsername("admin@example.com");
        user.setPassword("hashedPassword");
        user.setRoles(Set.of("ROLE_ADMIN", "ROLE_USER"));

        assertThat(user.getId()).isEqualTo(123L);
        assertThat(user.getUsername()).isEqualTo("admin@example.com");
        assertThat(user.getPassword()).isEqualTo("hashedPassword");
        assertThat(user.getRoles()).containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");
    }

    @Test
    void testUserRoleModification() {
        user.setRoles(Set.of("ROLE_USER"));
        assertThat(user.getRoles()).containsExactly("ROLE_USER");

        // Replace roles
        user.setRoles(Set.of("ROLE_ADMIN"));
        assertThat(user.getRoles()).containsExactly("ROLE_ADMIN");
        assertThat(user.getRoles()).doesNotContain("ROLE_USER");
    }

    @Test
    void testUsernameAndPasswordEdgeCases() {
        user.setUsername("");
        user.setPassword("");

        assertThat(user.getUsername()).isEqualTo("");
        assertThat(user.getPassword()).isEqualTo("");

        user.setUsername("u");
        user.setPassword("p");

        assertThat(user.getUsername()).isEqualTo("u");
        assertThat(user.getPassword()).isEqualTo("p");
    }
}