package com.example.backendcargoflow.domain.tripAssignment.mapper;

import com.example.backendcargoflow.controller.tripAssignment.models.AvailableDriverDto;
import com.example.backendcargoflow.domain.user.entity.AvailableDriverProjection;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface TripAssignmentMapper {
    AvailableDriverDto mapAvailableDriverProjectionToAvailableDriverDto(AvailableDriverProjection availableDriverProjection);

    List<AvailableDriverDto> mapAvailableDriverProjectionsToAvailableDriverDtos(List<AvailableDriverProjection> availableDriverProjections);
}
