package com.cards.assessment.app.service.impl;


import com.cards.assessment.app.domain.Card;
import com.cards.assessment.app.dto.CardDTO;
import com.cards.assessment.app.dto.UserDTO;
import com.cards.assessment.app.mapper.CardMapper;
import com.cards.assessment.app.mapper.UserMapper;
import com.cards.assessment.app.repository.CardRepository;
import com.cards.assessment.app.resource.errors.BadRequestAlertException;
import com.cards.assessment.app.service.CardService;
import com.cards.assessment.app.service.UserService;
import com.cards.assessment.app.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


@Service
@Transactional
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    private final String ENTITY_NAME = "Card";

    private final UserService userService;

    private final UserMapper userMapper;

    public CardServiceImpl(CardRepository cardRepository, CardMapper cardMapper, UserService userService, UserMapper userMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public CardDTO createCard(CardDTO cardDTO) {
        Card card = cardRepository.save(cardMapper.toEntity(cardDTO));
        return cardMapper.toDto(card);
    }

    @Override
    public CardDTO updateCard(CardDTO cardDTO) {
        Optional<Card> cardOptional = cardRepository.findById(cardDTO.getId());
        if(cardOptional.isEmpty()){
            throw new BadRequestAlertException("Invalid card id", ENTITY_NAME, "idnotfound");
        }
        Card card = cardRepository.save(cardMapper.toEntity(cardDTO));
        return cardMapper.toDto(card);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CardDTO> getCard(Long cardId) {
        if(!SecurityUtil.currentUserIsAdmin()){
            Optional<UserDTO> currentLoggedInUser = getCurrentLoggedInUser();
            return currentLoggedInUser.flatMap(userDTO -> cardRepository.findByUserAndId(userMapper.toEntity(userDTO), cardId).map(cardMapper::toDto));
        } else{
            return cardRepository.findById(cardId).map(cardMapper::toDto);
        }
    }

    @Override
    public void deleteById(Long cardId) {
        if(!SecurityUtil.currentUserIsAdmin()){
            Optional<UserDTO> currentLoggedInUser = getCurrentLoggedInUser();
            currentLoggedInUser.flatMap(userDTO -> cardRepository.findByUserAndId(userMapper.toEntity(userDTO), cardId)).ifPresent(cardRepository::delete);
        } else {
            cardRepository.deleteById(cardId);
        }
    }
    private Optional<UserDTO> getCurrentLoggedInUser() {
        return userService.getCurrentLoggedInUserDTO();
    }

}
