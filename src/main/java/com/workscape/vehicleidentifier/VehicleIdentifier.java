package com.workscape.vehicleidentifier;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

public class VehicleIdentifier {

    public static void main(String[] args) {
        try {
            XMLStreamReader reader = getXmlStreamReader();
            Map<String, List<Vehicle>> vehicleTypeWithCount = getVehicleTypeWithCount(reader);
            for(Map.Entry<String, List<Vehicle>> vehicleTypeEntry : vehicleTypeWithCount.entrySet()) {
                List<Vehicle> vehicles = vehicleTypeEntry.getValue();
                System.out.println("Vehicle Type: "+ vehicleTypeEntry.getKey() +", Vehicles: "+ vehicles.stream().map(Vehicle::getVehicleId).collect(Collectors.joining(","))+ " , Vehicle Count: "+ vehicles.size());
            };
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, List<Vehicle>> getVehicleTypeWithCount(XMLStreamReader reader) throws XMLStreamException {
        List<Vehicle> vehicles = processXML(reader);
        Map<String, List<Vehicle>> vehicleCountMap = new HashMap<>();
        vehicles.stream().forEach(vehicle -> {
            Optional<String> vehicleTypeOptional = VehicleTypeEnum.getVehicleType(vehicle);
            vehicleTypeOptional.ifPresent(vehicleType -> {
                if(vehicleCountMap.containsKey(vehicleType)) {
                    vehicleCountMap.get(vehicleType).add(vehicle);
                } else {
                    vehicleCountMap.put(vehicleType, newArrayList(vehicle));
                }
            });
        });

        return vehicleCountMap;
    }

    private static XMLStreamReader getXmlStreamReader() throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        return factory.createXMLStreamReader(ClassLoader.getSystemResourceAsStream("vehicles.xml"));
    }

    private static List<Vehicle> processXML(XMLStreamReader reader) throws XMLStreamException {
        List<Vehicle> vehicles = null;
        List<Wheel> wheels = null;
        Vehicle vehicle = null;
        Wheel wheel = null;
        String tagContent = null;
        String startElement = null;

        while(reader.hasNext()){
            int event = reader.next();

            switch(event){
                case XMLStreamConstants.START_ELEMENT:

                    if ("vehicle".equals(reader.getLocalName())){
                        vehicle = new Vehicle();
                    }
                    if("vehicles".equals(reader.getLocalName())){
                        vehicles = new ArrayList<Vehicle>();
                    }
                    if("wheels".equals(reader.getLocalName())){
                        wheels = new ArrayList<Wheel>();
                    }
                    if ("wheel".equals(reader.getLocalName())){
                        startElement = reader.getLocalName();
                        wheel = new Wheel();
                    }
                    if("powertrain".equals(reader.getLocalName())){
                        startElement = reader.getLocalName();
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    tagContent = reader.getText().trim();
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    if(vehicle != null) {
                        switch (reader.getLocalName()) {
                            case "vehicle":
                                vehicles.add(vehicle);
                                break;
                            case "id":
                                if (vehicle != null)
                                    vehicle.setVehicleId(tagContent);
                                break;
                            case "wheel":
                                wheels.add(wheel);
                                startElement = null;
                                break;
                            case "wheels":
                                vehicle.setWheels(wheels);
                                break;
                            case "position":
                                wheel.setPosition(tagContent);
                                break;
                            case "material":
                                if ("wheel".equals(startElement)) {
                                    wheel.setMaterial(tagContent);
                                } else {
                                    vehicle.setFrame(tagContent);
                                }
                                break;

                        }
                        if ("powertrain".equals(startElement)) {
                            vehicle.setPowerTrain(reader.getLocalName());
                            startElement = null;
                        }
                        break;
                    }
                case XMLStreamConstants.START_DOCUMENT:
                    vehicles = new ArrayList<Vehicle>();
                    break;
            }
        }
        return vehicles;
    }
}
