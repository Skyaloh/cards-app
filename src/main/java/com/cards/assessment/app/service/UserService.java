package com.cards.assessment.app.service;

import com.cards.assessment.app.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    Page<UserDTO>  getAllUsers(Pageable pageable);

    Optional<UserDTO> getUser(Long userId);

    Optional<UserDTO> getCurrentLoggedInUserDTO();
    void deleteById(Long userId );
}
