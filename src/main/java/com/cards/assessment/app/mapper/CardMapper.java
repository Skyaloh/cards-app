package com.cards.assessment.app.mapper;


import com.cards.assessment.app.domain.Card;
import com.cards.assessment.app.domain.User;
import com.cards.assessment.app.dto.CardDTO;
import com.cards.assessment.app.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CardMapper extends EntityMapper<CardDTO, Card> {
    @Mapping(source = "user.id", target = "userId")
    CardDTO toDto(Card card);
    @Mapping(source = "userId", target = "user")
    Card toEntity(CardDTO cardDTO);
    default Card fromId(Long id) {
        if (id == null) {
            return null;
        }
        Card card = new Card();
        card.setId(id);
        return card;
    }
}
