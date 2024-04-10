package com.cards.assessment.app.resource;

import com.cards.assessment.app.service.UserService;
import com.cards.assessment.app.dto.UserDTO;
import com.cards.assessment.app.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;

    private static final String ENTITY_NAME = "user";

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST  /create-user : Create a new User.
     *
     * @param userDTO the User to create
     * @return the ResponseEntity with status 201 (Created) and with body the new User, or with status 400 (Bad Request) if the user has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/create-user")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save new user : {}", userDTO);
        if (userDTO.getId() != null) {
           // throw new BadRequestAlertException("A new user cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserDTO result = userService.createUser(userDTO);

        return ResponseEntity
                .created(new URI("/api/v1/create-user/" + result.getId()))
                .body(result);
    }


    /**
     * GET  /user : get all the users.
     *
     * @param pageable for the pagination information
     * @return the ResponseEntity with status 200 (OK) and the Page of users in body
     */
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers(@PageableDefault(size = 20) final Pageable pageable) {
        log.debug("REST request to get a page of users");
        Page<UserDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok().body(users.getContent());
    }

    /**
     * GET /user/{id} : get the specific user.
     *
     * @param id the id of the user to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the user, or with status 404 (Not Found)
     */
    @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserDTO> getSingUser(@PathVariable  Long id) {
        log.debug("REST request to get user : {}", id);
        Optional<UserDTO> user = userService.getUser(id);
        return ResponseUtil.wrapOrNotFound(user);
    }

    /**
     * DELETE  /user/{id}: delete the "id" user.
     *
     * @param id the id of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/user/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.debug("REST request to delete user : {}", id);
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
