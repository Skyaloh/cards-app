package com.cards.assessment.app.service.impl;


import com.cards.assessment.app.domain.Card;
import com.cards.assessment.app.dto.CardDTO;
import com.cards.assessment.app.mapper.CardMapper;
import com.cards.assessment.app.repository.CardRepository;
import com.cards.assessment.app.resource.errors.BadRequestAlertException;
import com.cards.assessment.app.service.CardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;


@Service
@Transactional
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    private final String ENTITY_NAME = "Card";

    public CardServiceImpl(CardRepository cardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
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
        return cardRepository.findById(cardId).map(cardMapper::toDto);

    }

    @Override
    public void deleteById(Long cardId) {
        cardRepository.deleteById(cardId);
    }
}
