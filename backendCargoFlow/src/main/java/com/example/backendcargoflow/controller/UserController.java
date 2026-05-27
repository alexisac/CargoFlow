package com.example.backendcargoflow.controller;

import com.example.backendcargoflow.common.LogMessage;
import com.example.backendcargoflow.controller.user.api.UsersApi;
import com.example.backendcargoflow.controller.user.models.*;
import com.example.backendcargoflow.controller.common.models.GenericApplicationResponseDto;
import com.example.backendcargoflow.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UsersApi {
    private final UserService userService;

    @Override
    public GenericApplicationResponseDto addNewUser(@RequestBody AddNewUserRequestDto addNewUserRequestDto) {
        log.info(String.format(LogMessage.ADD_NEW_USER,
                addNewUserRequestDto.getFirstName(),
                addNewUserRequestDto.getLastName(),
                addNewUserRequestDto.getEmail(),
                addNewUserRequestDto.getRole().toString()
        ));
        return userService.addNewUser(addNewUserRequestDto);
    }

    @Override
    public LoginUserResponseDto loginUser(@RequestBody LoginUserRequestDto loginUserRequestDto) {
        log.info(String.format(LogMessage.LOGIN_USER,
                loginUserRequestDto.getEmail(),
                loginUserRequestDto.getHashedPassword()
        ));
        return userService.loginUser(loginUserRequestDto);
    }

    @Override
    public GetAllUsersResponseDto getAllUsers(
            Integer pageNumber,
            Integer pageSize
    ) {
        log.info(LogMessage.GET_ALL_USERS);
        return userService.getAllUsers(pageNumber, pageSize);
    }

    @Override
    public GenericApplicationResponseDto changeUserStatus(
            @PathVariable Long userId,
            @RequestBody ChangeUserStatusRequestDto changeUserStatusRequestDto
    ) {
        log.info(String.format(
                LogMessage.CHANGE_USER_STATUS,
                userId,
                changeUserStatusRequestDto.getActive()
        ));

        return userService.changeUserStatus(userId, changeUserStatusRequestDto);
    }
}
