package com.example.employee.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.example.employee.model.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUsername_ShouldReturnUser_WhenUserExists() {
        // Given
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRoles(Set.of("ROLE_USER"));

        entityManager.persistAndFlush(user);

        // When
        Optional<User> found = userRepository.findByUsername("testuser");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("testuser");
        assertThat(found.get().getPassword()).isEqualTo("password123");
        assertThat(found.get().getRoles()).contains("ROLE_USER");
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenUserDoesNotExist() {
        // When
        Optional<User> found = userRepository.findByUsername("nonexistent");

        // Then
        assertThat(found).isNotPresent();
    }

    @Test
    void existsByUsername_ShouldReturnTrue_WhenUserExists() {
        // Given
        User user = new User();
        user.setUsername("existinguser");
        user.setPassword("password123");
        user.setRoles(Set.of("ROLE_USER"));

        entityManager.persistAndFlush(user);

        // When
        boolean exists = userRepository.existsByUsername("existinguser");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByUsername_ShouldReturnFalse_WhenUserDoesNotExist() {
        // When
        boolean exists = userRepository.existsByUsername("nonexistent");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void save_ShouldPersistUser() {
        // Given
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("hashedpassword");
        user.setRoles(Set.of("ROLE_ADMIN"));

        // When
        User saved = userRepository.save(user);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("newuser");
        assertThat(saved.getPassword()).isEqualTo("hashedpassword");
        assertThat(saved.getRoles()).contains("ROLE_ADMIN");

        // Verify it's actually persisted
        User found = entityManager.find(User.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("newuser");
    }

    @Test
    void save_ShouldUpdateExistingUser() {
        // Given
        User user = new User();
        user.setUsername("updateuser");
        user.setPassword("oldpassword");
        user.setRoles(Set.of("ROLE_USER"));

        User saved = entityManager.persistAndFlush(user);
        entityManager.clear();

        // When
        saved.setPassword("newpassword");
        saved.setRoles(Set.of("ROLE_USER", "ROLE_ADMIN"));
        User updated = userRepository.save(saved);

        // Then
        assertThat(updated.getId()).isEqualTo(saved.getId());
        assertThat(updated.getPassword()).isEqualTo("newpassword");
        assertThat(updated.getRoles()).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");

        // Verify the update in the database
        entityManager.flush();
        entityManager.clear();
        User found = entityManager.find(User.class, saved.getId());
        assertThat(found.getPassword()).isEqualTo("newpassword");
        assertThat(found.getRoles()).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }

    @Test
    void findByUsername_ShouldBeCaseExact() {
        // Given
        User user = new User();
        user.setUsername("TestUser");
        user.setPassword("password123");
        user.setRoles(Set.of("ROLE_USER"));

        entityManager.persistAndFlush(user);

        // When
        Optional<User> foundExact = userRepository.findByUsername("TestUser");
        Optional<User> foundLower = userRepository.findByUsername("testuser");
        Optional<User> foundUpper = userRepository.findByUsername("TESTUSER");

        // Then
        assertThat(foundExact).isPresent();
        assertThat(foundLower).isNotPresent();
        assertThat(foundUpper).isNotPresent();
    }

    @Test
    void existsByUsername_ShouldBeCaseExact() {
        // Given
        User user = new User();
        user.setUsername("CaseSensitive");
        user.setPassword("password123");
        user.setRoles(Set.of("ROLE_USER"));

        entityManager.persistAndFlush(user);

        // When
        boolean existsExact = userRepository.existsByUsername("CaseSensitive");
        boolean existsLower = userRepository.existsByUsername("casesensitive");
        boolean existsUpper = userRepository.existsByUsername("CASESENSITIVE");

        // Then
        assertThat(existsExact).isTrue();
        assertThat(existsLower).isFalse();
        assertThat(existsUpper).isFalse();
    }

    @Test
    void findByUsername_ShouldHandleSpecialCharacters() {
        // Given
        User user = new User();
        user.setUsername("user@example.com");
        user.setPassword("password123");
        user.setRoles(Set.of("ROLE_USER"));

        entityManager.persistAndFlush(user);

        // When
        Optional<User> found = userRepository.findByUsername("user@example.com");

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("user@example.com");
    }

    @Test
    void save_ShouldHandleUserWithMultipleRoles() {
        // Given
        User user = new User();
        user.setUsername("multirole");
        user.setPassword("password123");
        user.setRoles(Set.of("ROLE_USER", "ROLE_ADMIN"));

        // When
        User saved = userRepository.save(user);

        // Then
        assertThat(saved.getRoles()).hasSize(2);
        assertThat(saved.getRoles()).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");

        // Verify persistence
        entityManager.flush();
        entityManager.clear();
        User found = entityManager.find(User.class, saved.getId());
        assertThat(found.getRoles()).hasSize(2);
        assertThat(found.getRoles()).containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }

    @Test
    void save_ShouldHandleUserWithEmptyRoles() {
        // Given
        User user = new User();
        user.setUsername("noroles");
        user.setPassword("password123");
        user.setRoles(Set.of());

        // When
        User saved = userRepository.save(user);

        // Then
        assertThat(saved.getRoles()).isEmpty();

        // Verify persistence
        entityManager.clear();
        User found = entityManager.find(User.class, saved.getId());
        assertThat(found.getRoles()).isEmpty();
    }

    @Test
    void delete_ShouldRemoveUser() {
        // Given
        User user = new User();
        user.setUsername("deleteme");
        user.setPassword("password123");
        user.setRoles(Set.of("ROLE_USER"));

        User saved = entityManager.persistAndFlush(user);

        // When
        userRepository.delete(saved);
        entityManager.flush();

        // Then
        User found = entityManager.find(User.class, saved.getId());
        assertThat(found).isNull();
        
        // Also verify by username
        Optional<User> foundByUsername = userRepository.findByUsername("deleteme");
        assertThat(foundByUsername).isNotPresent();
    }
}