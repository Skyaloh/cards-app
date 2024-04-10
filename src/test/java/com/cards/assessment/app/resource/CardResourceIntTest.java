package com.cards.assessment.app.resource;


import com.cards.assessment.app.IntegrationTest;
import com.cards.assessment.app.domain.Card;
import com.cards.assessment.app.domain.User;
import com.cards.assessment.app.dto.CardDTO;
import com.cards.assessment.app.mapper.CardMapper;
import com.cards.assessment.app.repository.CardRepository;
import com.cards.assessment.app.repository.UserRepository;
import com.cards.assessment.app.service.CardQueryService;
import com.cards.assessment.app.service.CardService;
import com.cards.assessment.app.service.UserService;
import com.cards.assessment.app.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.cards.assessment.app.util.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CardResource} REST controller.
 */
@IntegrationTest
public class CardResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String DEFAULT_COLOR = "CCCCCC";
    private static final String UPDATED_COLOR = "DDDDDD";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private CardService cardService;

    @Autowired
    private CardQueryService cardQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;


    @Autowired
    private EntityManager em;

    @Autowired
    private UserService userService;

    @Autowired
    private Validator validator;

    private MockMvc restCardMockMvc;

    @Autowired
    private UserRepository userRepository;

    private Card card;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        final CardResource cardResource = new CardResource(cardService, cardQueryService,userService);
        this.restCardMockMvc =
            MockMvcBuilders
                .standaloneSetup(cardResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setConversionService(createFormattingConversionService())
                .setMessageConverters(jacksonMessageConverter)
                .setValidator(validator)
                .build();

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("johndoe", "admin"));
        SecurityContextHolder.setContext(securityContext);
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Card createEntity(EntityManager em) {

        return new Card().name(DEFAULT_NAME).color(DEFAULT_COLOR).description(DEFAULT_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        card = createEntity(em);
    }

    @Test
    @Transactional
    void createCard() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();

        // Create the Card
        card.setUser(createUser());
        CardDTO cardDTO = cardMapper.toDto(card);
        restCardMockMvc
            .perform(
                post("/api/v1/card")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(TestUtil.convertObjectToJsonBytes(cardDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate + 1);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCard.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createCardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();

        // Create the Card with an existing ID
        card.setId(1L);
        CardDTO cardDTO = cardMapper.toDto(card);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardMockMvc
            .perform(
                post("/api/v1/card")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(TestUtil.convertObjectToJsonBytes(cardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    void getAllCards() throws Exception {
        // Initialize the database
        card.setUser(createUser());
        cardRepository.saveAndFlush(card);

        // Get all the cardList
        restCardMockMvc
            .perform(get("/api/v1/card?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCard() throws Exception {
        // Initialize the database
        card.setUser(createUser());
        cardRepository.saveAndFlush(card);

        // Get the card
        restCardMockMvc
            .perform(get("/api/v1/card/{id}", card.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(card.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getAllCardsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        card.setUser(createUser());
        cardRepository.saveAndFlush(card);

        // Get all the cardList where name equals to DEFAULT_NAME
        defaultCardShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the cardList where name equals to UPDATED_NAME
        defaultCardShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCardsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        card.setUser(createUser());
        cardRepository.saveAndFlush(card);

        // Get all the cardList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCardShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the cardList where name equals to UPDATED_NAME
        defaultCardShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCardsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        card.setUser(createUser());
        cardRepository.saveAndFlush(card);

        // Get all the cardList where name is not null
        defaultCardShouldBeFound("name.specified=true");

        // Get all the cardList where name is null
        defaultCardShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCardsByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        card.setUser(createUser());
        cardRepository.saveAndFlush(card);

        // Get all the cardList where color equals to DEFAULT_COLOR
        defaultCardShouldBeFound("color.equals=" + DEFAULT_COLOR);

        // Get all the cardList where color equals to UPDATED_COLOR
        defaultCardShouldNotBeFound("color.equals=" + UPDATED_COLOR);
    }

    @Transactional
     User createUser(){
        User user = UserResourceIntTest.createEntity(em);
        return userRepository.saveAndFlush(user);
    }

    @Test
    @Transactional
    void getAllCardsByColorInShouldWork() throws Exception {
        // Initialize the database
        card.setUser(createUser());
        cardRepository.saveAndFlush(card);

        // Get all the cardList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCardShouldBeFound("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR);

        // Get all the cardList where description equals to UPDATED_DESCRIPTION
        defaultCardShouldNotBeFound("color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllCardsByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        card.setUser(createUser());
        cardRepository.saveAndFlush(card);

        // Get all the cardList where description is not null
        defaultCardShouldBeFound("color.specified=true");

        // Get all the cardList where description is null
        defaultCardShouldNotBeFound("color.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCardShouldBeFound(String filter) throws Exception {
        restCardMockMvc
            .perform(get("/api/v1/card?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));

    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCardShouldNotBeFound(String filter) throws Exception {
        restCardMockMvc
            .perform(get("/api/v1/card?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

    }

    @Test
    @Transactional
    void getNonExistingCard() throws Exception {
        // Get the card
        restCardMockMvc.perform(get("/api/v1/card/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void updateCard() throws Exception {
        // Initialize the database
        card.setUser(createUser());
        cardRepository.saveAndFlush(card);

        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card
        Card updatedCard = cardRepository.findById(card.getId()).get();
        // Disconnect from session so that the updates on updatedCard are not directly saved in db
        em.detach(updatedCard);
        updatedCard.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        CardDTO cardDTO = cardMapper.toDto(updatedCard);

        restCardMockMvc
            .perform(
                put("/api/v1/card")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(TestUtil.convertObjectToJsonBytes(cardDTO))
            )
            .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCard.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    void deleteCard() throws Exception {
        // Initialize the database
        card.setUser(createUser());
        cardRepository.saveAndFlush(card);

        int databaseSizeBeforeDelete = cardRepository.findAll().size();

        // Get the card
        restCardMockMvc
            .perform(delete("/api/v1/card/{id}", card.getId()).accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Card.class);
        Card card1 = new Card();
        card1.setId(1L);
        Card card2 = new Card();
        card2.setId(card1.getId());
        assertThat(card1).isEqualTo(card2);
        card2.setId(2L);
        assertThat(card1).isNotEqualTo(card2);
        card1.setId(null);
        assertThat(card1).isNotEqualTo(card2);
    }

    @Test
    @Transactional
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CardDTO.class);
        CardDTO cardDTO1 = new CardDTO();
        cardDTO1.setId(1L);
        CardDTO cardDTO2 = new CardDTO();
        assertThat(cardDTO1).isNotEqualTo(cardDTO2);
        cardDTO2.setId(cardDTO1.getId());
        assertThat(cardDTO1).isEqualTo(cardDTO2);
        cardDTO2.setId(2L);
        assertThat(cardDTO1).isNotEqualTo(cardDTO2);
        cardDTO1.setId(null);
        assertThat(cardDTO1).isNotEqualTo(cardDTO2);
    }

    @Test
    @Transactional
    void testEntityFromId() {
        assertThat(cardMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cardMapper.fromId(null)).isNull();
    }
}
