package com.cards.assessment.app.service;

import com.cards.assessment.app.repository.UserRepository;
import com.cards.assessment.app.dto.UserDTO;
import com.cards.assessment.app.service.impl.UserServiceImpl;
import com.cards.assessment.app.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserServiceImplTest {

    @Mock
    private UserServiceImpl userServiceImpl;


    @Mock
    private UserRepository userRepository;


    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        userServiceImpl = new UserServiceImpl(userRepository,userMapper);
    }
    @Test
    void test_createUser_withValidData() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setFullName("John Doe");
        userDTO.setCountry("Kenya");
        userDTO.setPhone("1234567890");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPassword("password");
        userDTO.setAccountNonLocked(true);
        userDTO.setCredentialsNonExpired(true);
        userDTO.setAccountNonExpired(true);

        // Act
        UserDTO createdUser = userServiceImpl.createUser(userDTO);

        assertEquals(userDTO.getFullName(), createdUser.getFullName());
        assertEquals(userDTO.getCountry(), createdUser.getCountry());
        assertEquals(userDTO.getPhone(), createdUser.getPhone());
        assertEquals(userDTO.getEmail(), createdUser.getEmail());
        assertEquals(userDTO.getPassword(), createdUser.getPassword());
        assertEquals(userDTO.getAccountNonLocked(), createdUser.getAccountNonLocked());
        assertEquals(userDTO.getCredentialsNonExpired(), createdUser.getCredentialsNonExpired());
        assertEquals(userDTO.getAccountNonExpired(), createdUser.getAccountNonExpired());
    }
}
