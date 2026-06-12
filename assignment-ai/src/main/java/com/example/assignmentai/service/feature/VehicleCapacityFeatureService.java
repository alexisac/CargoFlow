package com.example.assignmentai.service.feature;

import com.example.assignmentai.model.assignment.AssignmentCandidate;
import com.example.assignmentai.model.assignment.VehicleTypeData;
import org.springframework.stereotype.Service;

@Service
public class VehicleCapacityFeatureService {

    public double calculateWeightUsageRatio(AssignmentCandidate candidate) {
        Double cargoWeight = candidate.cargoWeight();

        if (cargoWeight == null) {
            return 0.0;
        }

        Double maxWeight = resolveRelevantMaxWeight(candidate);

        if (maxWeight == null || maxWeight == 0.0) {
            return 0.0;
        }

        return cargoWeight / maxWeight;
    }

    public double calculateVolumeUsageRatio(AssignmentCandidate candidate) {
        Double cargoVolume = candidate.cargoVolume();

        if (cargoVolume == null) {
            return 0.0;
        }

        Double maxVolume = resolveRelevantMaxVolume(candidate);

        if (maxVolume == null || maxVolume == 0.0) {
            return 0.0;
        }

        return cargoVolume / maxVolume;
    }

    public double hasWeightCapacityFeature(AssignmentCandidate candidate) {
        return resolveRelevantMaxWeight(candidate) == null ? 0.0 : 1.0;
    }

    public double hasVolumeCapacityFeature(AssignmentCandidate candidate) {
        return resolveRelevantMaxVolume(candidate) == null ? 0.0 : 1.0;
    }

    public boolean isCapacityValid(AssignmentCandidate candidate) {
        return isWeightValid(candidate) && isVolumeValid(candidate);
    }

    private boolean isWeightValid(AssignmentCandidate candidate) {
        Double cargoWeight = candidate.cargoWeight();

        if (cargoWeight == null) {
            return true;
        }

        Double maxWeight = resolveRelevantMaxWeight(candidate);

        if (maxWeight == null) {
            return false;
        }

        return cargoWeight <= maxWeight;
    }

    private boolean isVolumeValid(AssignmentCandidate candidate) {
        Double cargoVolume = candidate.cargoVolume();

        if (cargoVolume == null) {
            return true;
        }

        Double maxVolume = resolveRelevantMaxVolume(candidate);

        if (maxVolume == null) {
            return false;
        }

        return cargoVolume <= maxVolume;
    }

    private Double resolveRelevantMaxWeight(AssignmentCandidate candidate) {
        VehicleTypeData primaryType = candidate.primaryVehicleType();

        if (primaryType == VehicleTypeData.TRACTOR_UNIT) {
            return candidate.trailerMaxWeight();
        }

        if (primaryType == VehicleTypeData.TANKER_TRUCK) {
            return null;
        }

        if (primaryType == VehicleTypeData.VAN ||
                primaryType == VehicleTypeData.BOX_TRUCK ||
                primaryType == VehicleTypeData.REFRIGERATED_TRUCK) {
            return candidate.primaryVehicleMaxWeight();
        }

        return candidate.primaryVehicleMaxWeight();
    }

    private Double resolveRelevantMaxVolume(AssignmentCandidate candidate) {
        VehicleTypeData primaryType = candidate.primaryVehicleType();

        if (primaryType == VehicleTypeData.TRACTOR_UNIT) {
            return candidate.trailerMaxVolume();
        }

        if (primaryType == VehicleTypeData.TANKER_TRUCK) {
            return candidate.primaryVehicleMaxVolume();
        }

        if (primaryType == VehicleTypeData.VAN ||
                primaryType == VehicleTypeData.BOX_TRUCK ||
                primaryType == VehicleTypeData.REFRIGERATED_TRUCK) {
            return candidate.primaryVehicleMaxVolume();
        }

        return candidate.primaryVehicleMaxVolume();
    }
}