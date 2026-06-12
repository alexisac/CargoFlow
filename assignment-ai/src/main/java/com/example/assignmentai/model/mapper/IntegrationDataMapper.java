package com.example.assignmentai.model.mapper;

import com.example.assignmentai.controller.cargoCoreInternal.models.CargoCoreAddressDataDto;
import com.example.assignmentai.model.assignment.AddressData;
import com.example.assignmentai.model.assignment.VehicleTypeData;
import org.springframework.stereotype.Component;

@Component
public class IntegrationDataMapper {

    public AddressData mapCargoCoreAddressToAddressData(CargoCoreAddressDataDto address) {
        if (address == null) {
            return null;
        }

        return new AddressData(
                address.getCountry(),
                address.getAdministrativeArea(),
                address.getCity(),
                address.getStreetName(),
                address.getStreetNumber(),
                address.getPostalCode(),
                address.getAdditionalDetails()
        );
    }

    public VehicleTypeData mapVehicleType(String vehicleType) {
        if (vehicleType == null) {
            return null;
        }

        return VehicleTypeData.valueOf(vehicleType);
    }
}