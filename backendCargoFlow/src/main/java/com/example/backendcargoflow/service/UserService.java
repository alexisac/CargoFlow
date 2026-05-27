package com.example.backendcargoflow.service;

import com.example.backendcargoflow.common.ErrorMessage;
import com.example.backendcargoflow.common.exceptions.NotFoundException;
import com.example.backendcargoflow.common.security.JWTService;
import com.example.backendcargoflow.common.exceptions.ConflictException;
import com.example.backendcargoflow.common.exceptions.UnauthorizedException;
import com.example.backendcargoflow.controller.user.models.*;
import com.example.backendcargoflow.controller.common.models.GenericApplicationResponseDto;
import com.example.backendcargoflow.domain.user.entity.User;
import com.example.backendcargoflow.domain.user.mapper.UserMapper;
import com.example.backendcargoflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JWTService jwtService;

    @PreAuthorize("hasRole('ADMIN')")
    public GenericApplicationResponseDto addNewUser(AddNewUserRequestDto addNewUserRequestDto) {
        Optional<User> user = userRepository.findByEmail(
                addNewUserRequestDto.getEmail()
        );
        if (user.isPresent()) {
           throw new ConflictException(ErrorMessage.USER_ALREADY_EXIST);
        }
        User newUser = userMapper.mapAddNewUserRequestDtoToUser(addNewUserRequestDto);
        userRepository.save(newUser);
        return GenericApplicationResponseFactory.success(
                "201 - USER_CREATED",
                "User was created successfully"
        );
    }

    public LoginUserResponseDto loginUser(LoginUserRequestDto loginUserRequestDto) {
        Optional<User> user = userRepository.findByEmailAndPassword(
                loginUserRequestDto.getEmail(),
                loginUserRequestDto.getHashedPassword()
        );

        if (user.isEmpty())
            throw new UnauthorizedException(ErrorMessage.INVALID_EMAIL_OR_PASSWORD);

        if (user.get().getActive() == false)
            throw new UnauthorizedException(ErrorMessage.USER_EXISTS_BUT_IS_INACTIVE);

        String accessToken = jwtService.generateToken(user.get());
        LoginUserResponseDto response = new LoginUserResponseDto();
        response.setAccessToken(accessToken);
        response.setTokenType("Bearer");

        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public GetAllUsersResponseDto getAllUsers(
            Integer pageNumber,
            Integer pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<User> userPage = userRepository.findAllByOrderByCreatedDateDesc(pageable);

        List<UserSummaryDto> userDtos = userMapper.mapUsersToUserSummaryDtos(userPage.getContent());

        GetAllUsersResponseDto response = new GetAllUsersResponseDto();
        response.setUsers(userDtos);
        response.setPageNumber(userPage.getNumber());
        response.setPageSize(userPage.getSize());
        response.setLastPage(userPage.isLast());

        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public GenericApplicationResponseDto changeUserStatus(
            Long userId,
            ChangeUserStatusRequestDto changeUserStatusRequestDto
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));

        user.setActive(changeUserStatusRequestDto.getActive());
        userRepository.save(user);

        return GenericApplicationResponseFactory.success(
                "200 - USER_STATUS_CHANGED",
                "User status was changed successfully"
        );
    }
}
