package com.cards.assessment.app.resource;


import com.cards.assessment.app.IntegrationTest;
import com.cards.assessment.app.domain.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.EntityManager;
import java.util.List;

@IntegrationTest
public class UserResourceIntTest {

    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String DEFAULT_COUNTRY = "kenya";
    private static final String DEFAULT_FULL_NAME = "John Doe";
    /**
     * Create a User.
     * <p>
     * This is a static method, as tests for other entities might also need it, if
     * they test an entity which has a required relationship to the User entity.
     */
    public static User createEntity(EntityManager em) {
        User user = new User();
        user.setEmail("example@gmail.com");
        user.setPassword(RandomStringUtils.random(60).replace("\u0000", "a"));
        user.setUserName("johndoe");
        user.setEmail(RandomStringUtils.randomAlphabetic(5) + DEFAULT_EMAIL);
        user.setCountry(DEFAULT_COUNTRY);
        user.setAccountNonExpired(false);
        user.setAccountNonLocked(false);
        user.setAccountNonExpired(false);
        user.setCredentialsNonExpired(false);
        user.setFullName(DEFAULT_FULL_NAME);
        user.setEnabled(true);
        return user;
    }
}
