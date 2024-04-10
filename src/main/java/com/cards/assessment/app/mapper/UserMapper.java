package com.cards.assessment.app.mapper;


import com.cards.assessment.app.domain.User;
import com.cards.assessment.app.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface UserMapper extends EntityMapper<UserDTO, User> {
    UserDTO toDto(User user);
    User toEntity(UserDTO userDTO);
    default User fromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
