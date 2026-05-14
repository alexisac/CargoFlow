package com.example.officeapp.features.addVehicle

import com.example.officeapp.models.vehicle.VehicleCapacityRequirement
import com.example.officeapp.models.vehicle.VehicleType

fun VehicleType.capacityRequirement(): VehicleCapacityRequirement {
    return when (this) {
        VehicleType.VAN,
        VehicleType.BOX_TRUCK,
        VehicleType.REFRIGERATED_TRUCK,
        VehicleType.SEMI_TRAILER,
        VehicleType.REFRIGERATED_TRAILER -> VehicleCapacityRequirement.WEIGHT_AND_VOLUME

        VehicleType.TANKER_TRUCK,
        VehicleType.TANKER_TRAILER -> VehicleCapacityRequirement.ONLY_VOLUME

        VehicleType.TRACTOR_UNIT -> VehicleCapacityRequirement.NONE
    }
}