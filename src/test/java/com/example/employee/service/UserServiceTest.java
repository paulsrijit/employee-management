package com.example.employee.service;

import com.example.employee.model.User;
import com.example.employee.repository.UserRepository;
import com.example.employee.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRoles(Set.of("ROLE_USER"));
    }

    @Test
    void register_ShouldReturnSavedUser_WhenUserIsValid() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword("encodedPassword");
        savedUser.setRoles(Set.of("ROLE_USER"));
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.register(user);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getPassword()).isEqualTo("encodedPassword");
        assertThat(result.getRoles()).contains("ROLE_USER");
        verify(userRepository).existsByUsername("testuser");
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_ShouldSetDefaultRole_WhenRolesAreNull() {
        user.setRoles(null);
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword("encodedPassword");
        savedUser.setRoles(Set.of("ROLE_USER"));
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.register(user);

        assertThat(result.getRoles()).contains("ROLE_USER");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_ShouldSetDefaultRole_WhenRolesAreEmpty() {
        user.setRoles(Set.of());
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword("encodedPassword");
        savedUser.setRoles(Set.of("ROLE_USER"));
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.register(user);

        assertThat(result.getRoles()).contains("ROLE_USER");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenUsernameExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertThatThrownBy(() -> userService.register(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Username already exists");
        
        verify(userRepository).existsByUsername("testuser");
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("testuser");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getPassword()).isEqualTo("password");
        assertThat(result.getAuthorities()).hasSize(1);
        assertThat(result.getAuthorities().iterator().next().getAuthority()).isEqualTo("ROLE_USER");
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUsername("nonexistent"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found: nonexistent");
        
        verify(userRepository).findByUsername("nonexistent");
    }

    @Test
    void loadUserByUsername_ShouldHandleMultipleRoles() {
        user.setRoles(Set.of("ROLE_USER", "ROLE_ADMIN"));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("testuser");

        assertThat(result.getAuthorities()).hasSize(2);
        assertThat(result.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))).isTrue();
        assertThat(result.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))).isTrue();
    }
}