package com.shelter.mykyda.security.mapper;

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

    @Mapping(target = "role", constant = "VOLUNTEER")
    User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto);

//    @Mapping(target = "displayName", source = "username")
//    @Mapping(target = "image", constant = "image")
//    @Mapping(target = "isEmailVerified", constant = "false")
//    @Mapping(target = "role", constant = "USER")
    UserDTO userToUserDto(User user);

}
