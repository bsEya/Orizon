package com.amaris.orizon.modules.user.mapper;

import com.amaris.orizon.modules.user.entity.User;
import com.amaris.orizon.modules.user.dto.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") // Pour pouvoir l'injecter avec @Autowired
public interface UserMapper {
    UserResponseDto toDto(User user);
    User toEntity(UserResponseDto dto);
}