package com.cards.assessment.app.service.impl;


import com.cards.assessment.app.domain.Authority;
import com.cards.assessment.app.domain.User;
import com.cards.assessment.app.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserLoginServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserLoginServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UsernameNotFoundException("Invalid email or password.");
        }
        List<SimpleGrantedAuthority> simpleGrantedAuthorityList = new ArrayList<>();
            userOptional.get().getAuthorities().forEach(authority -> {
              Authority userAuthority = (Authority) authority;
              simpleGrantedAuthorityList.add(new SimpleGrantedAuthority(userAuthority.getName()));
            });
        return new org.springframework.security.core.userdetails.User(userOptional.get().getEmail(), userOptional.get().getPassword(), simpleGrantedAuthorityList);
    }
}
