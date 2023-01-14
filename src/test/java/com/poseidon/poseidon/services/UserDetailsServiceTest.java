package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.User;
import com.poseidon.poseidon.domain.UserDetailsImpl;
import com.poseidon.poseidon.domain.UserRole;
import com.poseidon.poseidon.exceptions.UserNotFoundException;
import com.poseidon.poseidon.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration
public class UserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    void loadUserByUsername() {
        // Arrange
        User user = new User();
        user.setId(1);
        user.setRole(UserRole.User);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername("username");

        // Assert
        Assertions.assertTrue(userDetails instanceof UserDetailsImpl);
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
        Assertions.assertEquals(user.getId(), userDetailsImpl.getId());
        Assertions.assertEquals(1, userDetailsImpl.getAuthorities().size());
        Assertions.assertEquals(new SimpleGrantedAuthority("ROLE_USER"), ((List) userDetailsImpl.getAuthorities()).get(0));
        verify(userRepository, times(1)).findByUsername("username");
    }

    @Test
    void loadUserByUsernameNotFound() {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act
        assertThrows(UserNotFoundException.class, () -> userDetailsServiceImpl.loadUserByUsername("username"));

        // Assert
        verify(userRepository, times(1)).findByUsername("username");
    }
}