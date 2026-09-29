package com.amaris.orizon.modules.gestionuser.mapper;

import com.amaris.orizon.modules.gestionuser.entity.User;
import com.amaris.orizon.modules.gestionuser.dto.UserResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", implementationName = "GestionUserMapperImpl") // Pour pouvoir l'injecter avec @Autowired
public interface UserMapper {
    UserResponseDto toDto(User user);
    User toEntity(UserResponseDto dto);
}