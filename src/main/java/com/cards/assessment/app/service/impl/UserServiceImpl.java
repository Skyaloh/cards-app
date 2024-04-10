package com.cards.assessment.app.service.impl;

import com.cards.assessment.app.domain.User;
import com.cards.assessment.app.repository.UserRepository;
import com.cards.assessment.app.service.UserService;
import com.cards.assessment.app.dto.UserDTO;
import com.cards.assessment.app.mapper.UserMapper;
import com.cards.assessment.app.util.SecurityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = userRepository.save(userMapper.toEntity(userDTO));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> getCurrentLoggedInUserDTO() {
        return SecurityUtil.getCurrentUserLogin().flatMap(userRepository::findByEmail).map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUser(Long userId) {
        return userRepository.findById(userId).map(userMapper::toDto);

    }

    @Override
    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }
}
