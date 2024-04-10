package com.cards.assessment.app.resource;

import com.cards.assessment.app.dto.CardDTO;
import com.cards.assessment.app.helper.filter.LongFilter;
import com.cards.assessment.app.resource.errors.BadRequestAlertException;
import com.cards.assessment.app.service.CardQueryService;
import com.cards.assessment.app.service.CardService;
import com.cards.assessment.app.service.UserService;
import com.cards.assessment.app.service.criteria.CardCriteria;
import com.cards.assessment.app.util.ResponseUtil;
import com.cards.assessment.app.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1")
public class CardResource {

    private final Logger log = LoggerFactory.getLogger(CardResource.class);

    private final CardService cardService;

    private final CardQueryService cardQueryService;

    private final UserService userService;

    private static final String ENTITY_NAME = "card";

    public CardResource(CardService cardService, CardQueryService cardQueryService, UserService userService) {
        this.cardService = cardService;
        this.cardQueryService = cardQueryService;
        this.userService = userService;
    }

    /**
     * POST  /create-card : Create a new Card.
     *
     * @param cardDTO the Card to create
     * @return the ResponseEntity with status 201 (Created) and with body the new Card, or with status 400 (Bad Request) if the card has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/card")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') or hasAnyAuthority('ROLE_MEMBER')")
    public ResponseEntity<CardDTO> createCard(@Valid @RequestBody CardDTO cardDTO) throws URISyntaxException {
        log.debug("REST request to save new card : {}", cardDTO);
        if (cardDTO.getId() != null) {
            throw new BadRequestAlertException("A new card cannot already have an ID", ENTITY_NAME, "idexists");
        }

        userService.getCurrentLoggedInUserDTO().ifPresent(userDTO ->
                cardDTO.setUserId(userDTO.getId()));

        CardDTO result = cardService.createCard(cardDTO);

        return ResponseEntity
                .created(new URI("/api/v1/create-card/" + result.getId()))
                .body(result);
    }

    /**
     * PUT  / : Update a Card.
     *
     * @param cardDTO the Card to Update
     * @return the ResponseEntity with status 200 (Ok) and with body the updated Card, or with status 400 (Bad Request) if the card has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/card")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') or hasAnyAuthority('ROLE_MEMBER')")
    public ResponseEntity<CardDTO> updateCard(@Valid @RequestBody CardDTO cardDTO) throws URISyntaxException {
        log.debug("REST request to update a card : {}", cardDTO);
        userService.getCurrentLoggedInUserDTO().ifPresent(userDTO ->
                cardDTO.setUserId(userDTO.getId()));
        CardDTO result = cardService.updateCard(cardDTO);
        return ResponseEntity.ok().body(result);
    }



    /**
     * GET  /card : get all the cards.
     *
     * @param pageable for the pagination information
     * @return the ResponseEntity with status 200 (OK) and the Page of cards in body
     */
    @GetMapping(value = "/card", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') or hasAnyAuthority('ROLE_MEMBER')")
    public ResponseEntity<List<CardDTO>> getAllCards(CardCriteria cardCriteria,
                                                     @PageableDefault(size = 20) final Pageable pageable) {
        log.debug("REST request to get a page of cards");
        if(!SecurityUtil.currentUserIsAdmin()){
            cardCriteria.setUserId(new LongFilter());
            userService.getCurrentLoggedInUserDTO().ifPresent(userDTO ->
                    cardCriteria.getUserId().setEquals(userDTO.getId()));
        }
        Page<CardDTO> cards = cardQueryService.findByCriteria(cardCriteria,pageable);
        return ResponseEntity.ok().body(cards.getContent());
    }

    /**
     * GET /card/{id} : get the specific card.
     *
     * @param id the id of the card to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the card, or with status 404 (Not Found)
     */
    @GetMapping(value = "/card/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') or hasAnyAuthority('ROLE_MEMBER')")
    public ResponseEntity<CardDTO> getSingleCard(@PathVariable  Long id) {
        log.debug("REST request to get card : {}", id);
        Optional<CardDTO> card = cardService.getCard(id);
        return ResponseUtil.wrapOrNotFound(card);
    }

    /**
     * DELETE  /card/{id}: delete the "id" card.
     *
     * @param id the id of the card to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/card/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN') or hasAnyAuthority('ROLE_MEMBER')")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        log.debug("REST request to delete card : {}", id);
        cardService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
