package com.poseidon.poseidon.services;


import com.poseidon.poseidon.domain.User;
import com.poseidon.poseidon.domain.UserRole;
import com.poseidon.poseidon.exceptions.NewUserWithEmptyPasswordException;
import com.poseidon.poseidon.exceptions.UserNotFoundException;
import com.poseidon.poseidon.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    final int USER_ID = 1;
    final String USER_FULL_NAME = "full name";
    final String USER_NAME = "user name";
    final String USER_PASSWORD = "password";
    final String USER_PREVIOUS_PASSWORD = "previousPassword";
    final UserRole USER_ROLE = UserRole.User;

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testFindAll() {
        // Act
        service.findAll();

        // Assert
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testSave() {
        // Arrange
        User newUser = new User();
        newUser.setFullname(USER_FULL_NAME);
        newUser.setUsername(USER_NAME);
        newUser.setRole(USER_ROLE);
        newUser.setPassword(USER_PASSWORD);
        when(passwordEncoder.encode(anyString())).thenReturn(USER_PASSWORD);

        // Act
        service.save(newUser);

        // Assert
        ArgumentCaptor<User> user = ArgumentCaptor.forClass(User.class);
        verify(repository, times(1)).save(user.capture());
        assertEquals(USER_FULL_NAME, user.getValue().getFullname());
        assertEquals(USER_NAME, user.getValue().getUsername());
        assertEquals(USER_PASSWORD, user.getValue().getPassword());
        assertEquals(USER_ROLE, user.getValue().getRole());
        verify(passwordEncoder, times(1)).encode(USER_PASSWORD);
    }

    @Test
    public void testSavePasswordException() {
        // Arrange
        User newUser = new User();
        newUser.setFullname(USER_FULL_NAME);
        newUser.setUsername(USER_NAME);
        newUser.setRole(USER_ROLE);
        newUser.setPassword("");

        // Act
        assertThrows(NewUserWithEmptyPasswordException.class, () -> service.save(newUser));

        // Assert
        verify(repository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    public void testUpdate() {
        // Arrange
        User userFromRepo = new User();
        userFromRepo.setId(USER_ID);
        when(repository.findById(USER_ID)).thenReturn(Optional.of(userFromRepo));
        User updatedUser = new User();
        updatedUser.setFullname(USER_FULL_NAME);
        updatedUser.setUsername(USER_NAME);
        updatedUser.setRole(USER_ROLE);
        updatedUser.setPassword(USER_PASSWORD);
        when(passwordEncoder.encode(anyString())).thenReturn(USER_PASSWORD);


        // Act
        service.update(updatedUser, USER_ID);

        // Assert
        ArgumentCaptor<User> user = ArgumentCaptor.forClass(User.class);
        verify(repository, times(1)).save(user.capture());
        assertEquals(USER_ID, user.getValue().getId(), 0);
        assertEquals(USER_FULL_NAME, user.getValue().getFullname());
        assertEquals(USER_NAME, user.getValue().getUsername());
        assertEquals(USER_PASSWORD, user.getValue().getPassword());
        assertEquals(USER_ROLE, user.getValue().getRole());
        verify(repository, times(1)).findById(USER_ID);
        verify(passwordEncoder, times(1)).encode(USER_PASSWORD);
    }

    @Test
    public void testUpdateExceptPassword() {
        // Arrange
        User userFromRepo = new User();
        userFromRepo.setId(USER_ID);
        userFromRepo.setPassword(USER_PREVIOUS_PASSWORD);
        when(repository.findById(USER_ID)).thenReturn(Optional.of(userFromRepo));
        User updatedUser = new User();
        updatedUser.setFullname(USER_FULL_NAME);
        updatedUser.setUsername(USER_NAME);
        updatedUser.setRole(USER_ROLE);
        updatedUser.setPassword("");

        // Act
        service.update(updatedUser, USER_ID);

        // Assert
        ArgumentCaptor<User> user = ArgumentCaptor.forClass(User.class);
        verify(repository, times(1)).save(user.capture());
        assertEquals(USER_ID, user.getValue().getId(), 0);
        assertEquals(USER_FULL_NAME, user.getValue().getFullname());
        assertEquals(USER_NAME, user.getValue().getUsername());
        assertEquals(USER_PREVIOUS_PASSWORD, user.getValue().getPassword());
        assertEquals(USER_ROLE, user.getValue().getRole());
        verify(repository, times(1)).findById(USER_ID);
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    public void testFindById() {
        // Arrange
        User userFromRepo = new User();
        userFromRepo.setId(USER_ID);
        when(repository.findById(USER_ID)).thenReturn(Optional.of(userFromRepo));

        // Act
        User user = service.findById(USER_ID);

        // Assert
        assertEquals(USER_ID, user.getId(), 0);
        verify(repository, times(1)).findById(USER_ID);
    }

    @Test
    public void testGetUserById() {
        // Arrange
        User userFromRepo = new User();
        userFromRepo.setId(USER_ID);
        userFromRepo.setPassword(USER_PASSWORD);
        when(repository.findById(USER_ID)).thenReturn(Optional.of(userFromRepo));

        // Act
        User user = service.getUserById(USER_ID);

        // Assert
        assertEquals(USER_ID, user.getId(), 0);
        assertEquals("", user.getPassword());
        verify(repository, times(1)).findById(USER_ID);
    }

    @Test
    public void testFindByIdNotFound() {
        // Arrange

        when(repository.findById(USER_ID)).thenReturn(Optional.empty());

        // Act
        assertThrows(UserNotFoundException.class, () -> service.findById(USER_ID));

        // Assert
        verify(repository, times(1)).findById(USER_ID);
    }

    @Test
    public void testDelete() {
        // Arrange
        User userFromRepo = new User();
        userFromRepo.setId(USER_ID);
        when(repository.findById(USER_ID)).thenReturn(Optional.of(userFromRepo));

        // Act
        service.delete(USER_ID);

        // Assert
        verify(repository, times(1)).deleteById(USER_ID);
    }
}
