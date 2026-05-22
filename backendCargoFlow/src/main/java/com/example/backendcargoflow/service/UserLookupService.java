package com.example.backendcargoflow.service;

import com.example.backendcargoflow.common.ErrorMessage;
import com.example.backendcargoflow.common.exceptions.NotFoundException;
import com.example.backendcargoflow.domain.user.entity.UserFullNameProjection;
import com.example.backendcargoflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLookupService {
    private final UserRepository userRepository;

    public String getUserFullNameById(Long userId) {
        UserFullNameProjection fullName = userRepository.findFullNameProjectionById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.USER_NOT_FOUND));
        return fullName.getFirstName() + " " + fullName.getLastName();
    }
}
