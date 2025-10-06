package com.example.employee.model;

import com.example.employee.model.Role;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleTest {

    @Test
    void testRoleEnumValues() {
        Role[] roles = Role.values();
        
        assertThat(roles).hasSize(2);
        assertThat(roles).containsExactlyInAnyOrder(Role.ROLE_USER, Role.ROLE_ADMIN);
    }

    @Test
    void testRoleUser() {
        Role role = Role.ROLE_USER;
        
        assertThat(role).isEqualTo(Role.ROLE_USER);
        assertThat(role.name()).isEqualTo("ROLE_USER");
        assertThat(role.toString()).isEqualTo("ROLE_USER");
    }

    @Test
    void testRoleAdmin() {
        Role role = Role.ROLE_ADMIN;
        
        assertThat(role).isEqualTo(Role.ROLE_ADMIN);
        assertThat(role.name()).isEqualTo("ROLE_ADMIN");
        assertThat(role.toString()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void testRoleValueOf() {
        Role userRole = Role.valueOf("ROLE_USER");
        Role adminRole = Role.valueOf("ROLE_ADMIN");
        
        assertThat(userRole).isEqualTo(Role.ROLE_USER);
        assertThat(adminRole).isEqualTo(Role.ROLE_ADMIN);
    }

    @Test
    void testRoleComparison() {
        assertThat(Role.ROLE_USER).isNotEqualTo(Role.ROLE_ADMIN);
        assertThat(Role.ROLE_USER).isEqualTo(Role.ROLE_USER);
        assertThat(Role.ROLE_ADMIN).isEqualTo(Role.ROLE_ADMIN);
    }

    @Test
    void testRoleOrdinal() {
        // Test that the enum maintains its order
        assertThat(Role.ROLE_USER.ordinal()).isEqualTo(0);
        assertThat(Role.ROLE_ADMIN.ordinal()).isEqualTo(1);
    }
}