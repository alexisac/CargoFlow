package com.example.assignmentai.model.mapper;

import com.example.assignmentai.controller.assignmentai.models.AddressDto;
import com.example.assignmentai.controller.assignmentai.models.AssignmentCandidateDto;
import com.example.assignmentai.controller.assignmentai.models.OptimalAssignmentResponseDto;
import com.example.assignmentai.controller.assignmentai.models.VehicleTypeDto;
import com.example.assignmentai.model.assignment.AddressData;
import com.example.assignmentai.model.assignment.AssignmentCandidate;
import com.example.assignmentai.model.assignment.OptimalAssignmentResult;
import com.example.assignmentai.model.assignment.VehicleTypeData;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AssignmentAiMapper {
    AssignmentCandidate mapAssignmentCandidateDtoToAssignmentCandidate(
            AssignmentCandidateDto assignmentCandidateDto
    );

    List<AssignmentCandidate> mapAssignmentCandidateDtosToAssignmentCandidates(
            List<AssignmentCandidateDto> assignmentCandidateDtos
    );

    AddressData mapAddressDtoToAddressData(AddressDto addressDto);

    OptimalAssignmentResponseDto mapOptimalAssignmentResultToOptimalAssignmentResponseDto(
            OptimalAssignmentResult optimalAssignmentResult
    );

    default VehicleTypeData mapVehicleType(VehicleTypeDto vehicleTypeDto) {
        return vehicleTypeDto == null ? null : VehicleTypeData.valueOf(vehicleTypeDto.name());
    }

    default VehicleTypeDto mapVehicleType(VehicleTypeData vehicleTypeData) {
        return vehicleTypeData == null ? null : VehicleTypeDto.valueOf(vehicleTypeData.name());
    }

    default <T> T map(JsonNullable<T> value) {
        return value == null || !value.isPresent() ? null : value.get();
    }

    default <T> JsonNullable<T> map(T value) {
        return JsonNullable.of(value);
    }
}