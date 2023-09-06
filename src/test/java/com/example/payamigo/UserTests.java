package com.example.payamigo;

import com.example.payamigo.model.User;
import com.example.payamigo.repository.UserRepository;
import com.example.payamigo.service.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UsersService usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void addUser() {
        User newUser = new User(1, "Malina", "malina@example.com","123456");

        when(userRepository.save(any())).thenReturn(newUser);

        User result = usersService.addUser(newUser);

        assertEquals(newUser, result);

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void getNoOfUser() {

        long expectedNoOfUsers = 5;

        when(userRepository.count()).thenReturn(expectedNoOfUsers);

        long result = usersService.getNoOfUser();

        assertEquals(expectedNoOfUsers, result);

        verify(userRepository, times(1)).count();
    }

    @Test
    void removeUser() {
        int userIdToRemove = 1;
        User userToRemove = new User(userIdToRemove, "Malina", "malina@example.com", "123456");

        when(userRepository.findById((Integer) userIdToRemove)).thenReturn(Optional.of(userToRemove));

        boolean result = usersService.deleteUser(userIdToRemove);

        assertTrue(result);

        verify(userRepository, times(1)).delete(userToRemove);
    }

    @Test
    void getUserById() {
        Integer userId = 1;
        User expectedUser = new User((Integer) userId, "Malina", "malina@example.com","123456");

        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User result = usersService.getUserById(userId);

        assertEquals(expectedUser, result);

        verify(userRepository, times(1)).findById(userId);
    }
    @Test
    void getUserByName() {
        String userName = "Malina";
        User expectedUser = new User(1, userName, "malina@example.com","123456");

        when(userRepository.findByName(userName)).thenReturn(expectedUser);

        User result = usersService.getUserByName(userName);

        assertEquals(expectedUser, result);

        verify(userRepository, times(1)).findByName(userName);
    }
}
