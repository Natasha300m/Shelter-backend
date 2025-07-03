package com.shelter.mykyda.security.mapper;

import com.shelter.mykyda.database.entity.Role;
import com.shelter.mykyda.dto.UserDTO;
import com.shelter.mykyda.database.entity.User;
import com.shelter.mykyda.security.dto.UserGetDto;
import com.shelter.mykyda.security.dto.UserRegistrationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_DEFAULT)
public interface UserMapper {

    UserGetDto userToUserGetDto(User user);

    User userGetDtoToUser(UserGetDto userGetDto);

    @Mapping(target = "role", expression = "java(convertToRole(userRegistrationDto.getRole()))")
    User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto);

    default Role convertToRole(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    UserDTO userToUserDto(User user);

}
