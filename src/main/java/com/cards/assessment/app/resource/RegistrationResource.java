package com.cards.assessment.app.resource;



import com.cards.assessment.app.service.UserService;
import com.cards.assessment.app.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1")

public class RegistrationResource {

    private final UserService userService;


    public RegistrationResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public ResponseEntity<UserDTO> registration(@RequestBody UserDTO userDTO) throws URISyntaxException {
        userService.createUser(userDTO);
        UserDTO result = userService.createUser(userDTO);

        return ResponseEntity
                .created(new URI("/api/v1/registration/" + result.getId()))
                .body(result);
    }
}
