package com.security.securityImpl.security.mapper;

import com.security.securityImpl.security.dto.response.UsersResponseDto;
import com.security.securityImpl.security.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UsersMapper {


    @Mapping(target = "password", ignore = true)
//    @Mapping(target = "role", ignore = true)
    @Mapping(target = "tokenList", ignore = true)
    UsersResponseDto mapToUserResponseDto(@MappingTarget UsersResponseDto usersResponseDto, Users users);


}
