package com.example.assignmentai.service.candidate;

import com.example.assignmentai.controller.cargoCoreInternal.models.CargoCoreAssignmentContextResponseDto;
import com.example.assignmentai.controller.cargoCoreInternal.models.CargoCoreDriverDataDto;
import com.example.assignmentai.controller.cargoCoreInternal.models.CargoCoreVehicleDataDto;
import com.example.assignmentai.model.assignment.AssignmentCandidate;
import com.example.assignmentai.model.assignment.VehicleTypeData;
import com.example.assignmentai.model.location.DriverLocationData;
import com.example.assignmentai.model.location.LatestDriverLocationsResponse;
import com.example.assignmentai.model.mapper.IntegrationDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateGenerationService {
    private static final int MAX_CANDIDATES = 100;

    private final IntegrationDataMapper integrationDataMapper;

    public List<AssignmentCandidate> generateCandidates(
            CargoCoreAssignmentContextResponseDto context,
            LatestDriverLocationsResponse latestDriverLocationsResponse
    ) {
        Map<Long, DriverLocationData> locationByDriverId = latestDriverLocationsResponse.driverLocations()
                .stream()
                .collect(Collectors.toMap(
                        DriverLocationData::driverId,
                        location -> location,
                        (first, ignored) -> first
                ));

        List<AssignmentCandidate> candidates = new ArrayList<>();

        for (CargoCoreDriverDataDto driver : context.getDrivers()) {
            if (!Boolean.TRUE.equals(driver.getDriverAvailable())) {
                continue;
            }

            DriverLocationData driverLocation = locationByDriverId.get(driver.getDriverId());

            if (driverLocation == null) {
                continue;
            }

            addCandidatesForDriver(candidates, context, driver, driverLocation);

            if (candidates.size() >= MAX_CANDIDATES) {
                return candidates.subList(0, MAX_CANDIDATES);
            }
        }

        return candidates;
    }

    private void addCandidatesForDriver(
            List<AssignmentCandidate> candidates,
            CargoCoreAssignmentContextResponseDto context,
            CargoCoreDriverDataDto driver,
            DriverLocationData driverLocation
    ) {
        for (CargoCoreVehicleDataDto primaryVehicle : context.getPrimaryVehicles()) {
            VehicleTypeData primaryVehicleType = integrationDataMapper.mapVehicleType(primaryVehicle.getVehicleType());

            if (primaryVehicleType == VehicleTypeData.TRACTOR_UNIT)
                addTractorUnitCandidates(candidates, context, driver, driverLocation, primaryVehicle);
            else
                candidates.add(buildCandidate(context, driver, driverLocation, primaryVehicle, null));
        }
    }

    private void addTractorUnitCandidates(
            List<AssignmentCandidate> candidates,
            CargoCoreAssignmentContextResponseDto context,
            CargoCoreDriverDataDto driver,
            DriverLocationData driverLocation,
            CargoCoreVehicleDataDto primaryVehicle
    ) {
        for (CargoCoreVehicleDataDto trailer : context.getTrailers()) {
            candidates.add(buildCandidate(context, driver, driverLocation, primaryVehicle, trailer));
        }
    }

    private AssignmentCandidate buildCandidate(
            CargoCoreAssignmentContextResponseDto context,
            CargoCoreDriverDataDto driver,
            DriverLocationData driverLocation,
            CargoCoreVehicleDataDto primaryVehicle,
            CargoCoreVehicleDataDto trailer
    ) {
        VehicleTypeData primaryVehicleType = integrationDataMapper.mapVehicleType(primaryVehicle.getVehicleType());

        VehicleTypeData trailerType = trailer == null ? null : integrationDataMapper.mapVehicleType(trailer.getVehicleType());

        boolean trailerRequired = primaryVehicleType == VehicleTypeData.TRACTOR_UNIT;

        return new AssignmentCandidate(
                context.getTrip().getTripId(),
                driver.getDriverId(),
                primaryVehicle.getVehicleId(),
                trailer == null ? null : trailer.getVehicleId(),

                driverLocation.latitude(),
                driverLocation.longitude(),
                integrationDataMapper.mapCargoCoreAddressToAddressData(context.getTrip().getPickupAddress()),

                context.getTrip().getCargoWeight(),
                context.getTrip().getCargoVolume(),

                primaryVehicleType,
                primaryVehicle.getMaxWeight(),
                primaryVehicle.getMaxVolume(),

                trailerType,
                trailer == null ? null : trailer.getMaxWeight(),
                trailer == null ? null : trailer.getMaxVolume(),

                primaryVehicle.getVehicleId().equals(driver.getLastPrimaryVehicleId()),
                trailer != null && trailer.getVehicleId().equals(driver.getLastTrailerId()),
                trailerRequired,
                Boolean.TRUE.equals(driver.getDriverAvailable()),
                true,
                trailer != null,

                calculateMinutesUntilPickup(context.getTrip().getPickupInstant()),
                driver.getDriverCompletedTripsLast30Days()
        );
    }

    private Integer calculateMinutesUntilPickup(OffsetDateTime pickupInstant) {
        if (pickupInstant == null) {
            return 0;
        }

        long minutes = Duration.between(OffsetDateTime.now(), pickupInstant).toMinutes();

        return Math.max((int) minutes, 0);
    }
}