package com.workscape.vehicleidentifier;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

public enum VehicleTypeEnum {
    BIG_WHEEL("Big Wheel","plastic","human", "plastic", newArrayList("front", "left rear", "right rear")),
    BICYCLE("Bicycle", "metal", "human", "metal", newArrayList("front", "rear")),
    MOTORCYCLE("Motorcycle", "metal", "Internal Combustion", "metal", newArrayList("front", "rear")),
    HANG_GLIDER("Hang Glider", "plastic", "Bernoulli", "", newArrayList()),
    CAR("Car", "metal", "Internal Combustion", "metal", newArrayList("left front", "left rear", "right front", "right rear"));

    private String vehicleType;
    private String frame;
    private String powertrain;
    private String wheelMaterial;
    private List<String> wheelPositions;

    VehicleTypeEnum(String vehicleType, String frame, String powertrain, String wheelMaterial, List<String> wheelPositions) {
        this.vehicleType = vehicleType;
        this.frame = frame;
        this.powertrain = powertrain;
        this.wheelMaterial = wheelMaterial;
        this.wheelPositions = wheelPositions;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getFrame() {
        return frame;
    }

    public String getPowertrain() {
        return powertrain;
    }

    public String getWheelMaterial() {
        return wheelMaterial;
    }

    public List<String> getWheelPositions() {
        return wheelPositions;
    }

    public static Optional<String> getVehicleType(Vehicle vehicle) {
        for(final VehicleTypeEnum vehicleType: values()) {
            if(Objects.nonNull(vehicle.getWheels())) {
                if (vehicleType.getFrame().equalsIgnoreCase(vehicle.getFrame())
                        && vehicleType.getPowertrain().equalsIgnoreCase(vehicle.getPowerTrain())
                        && (vehicleType.getWheelPositions()
                        .containsAll(vehicle.getWheels().stream().map(Wheel::getPosition).collect(Collectors.toList())))
                        && newArrayList(vehicleType.getWheelMaterial()).containsAll(vehicle.getWheels().stream().map(Wheel::getMaterial)
                        .collect(Collectors.toSet()))
                        && vehicle.getWheels().stream().map(Wheel::getMaterial)
                        .collect(Collectors.toList()).size() == (vehicle.getWheels().size())) {

                    return Optional.of(vehicleType.getVehicleType());
                }
            }
        }
        return Optional.empty();
    }
}
