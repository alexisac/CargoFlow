package com.example.backendcargoflow.domain.user.mapper;

import com.example.backendcargoflow.controller.user.models.AddNewUserRequestDto;
import com.example.backendcargoflow.controller.user.models.UserRoleDto;
import com.example.backendcargoflow.domain.user.entity.User;
import com.example.backendcargoflow.domain.user.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "hashedPassword")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "active", expression = "java(Boolean.TRUE)")
    User mapAddNewUserRequestDtoToUser(AddNewUserRequestDto addNewUserRequestDto);

    default UserRole map(UserRoleDto roleDto) {
        return roleDto == null ? null : UserRole.valueOf(roleDto.name());
    }
}
