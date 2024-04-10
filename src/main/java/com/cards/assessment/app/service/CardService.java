package com.cards.assessment.app.service;

import com.cards.assessment.app.dto.CardDTO;
import java.util.Optional;

public interface CardService {
    CardDTO createCard(CardDTO cardDTO);
    CardDTO updateCard(CardDTO cardDTO);
    Optional<CardDTO> getCard(Long cardId);

    void deleteById(Long cardId );
}
