package com.workscape.vehicleidentifier;


import org.junit.Before;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * Unit test for simple App.
 */
public class VehicleIdentifierTest {
	private VehicleIdentifier vehicleIdentifier;

	@Before
	public void setUp () {
		vehicleIdentifier = new VehicleIdentifier();
	}

	@Test
	public void shouldReturnTypeWithCountOfVehicles() throws XMLStreamException {
		XMLStreamReader xmlStreamReader = getXmlStreamReader("sample-vehicle.xml");

		Map<String, List<Vehicle>> vehicleTypeWithCount = VehicleIdentifier.getVehicleTypeWithCount(xmlStreamReader);

		assertTrue(vehicleTypeWithCount.containsKey("Big Wheel"));
		assertEquals("vehicle 1", vehicleTypeWithCount.get("Big Wheel").get(0).getVehicleId());
		assertEquals(1, vehicleTypeWithCount.get("Big Wheel").size());
		assertTrue(vehicleTypeWithCount.containsKey("Bicycle"));
		assertEquals("vehicle 2", vehicleTypeWithCount.get("Bicycle").get(0).getVehicleId());
		assertEquals("vehicle 3", vehicleTypeWithCount.get("Bicycle").get(1).getVehicleId());
		assertEquals(2, vehicleTypeWithCount.get("Bicycle").size());
	}

	@Test
	public void shouldNotReturnTypeWithCountOfVehiclesIfNoMatchingVehicleFound() throws XMLStreamException {
		XMLStreamReader xmlStreamReader = getXmlStreamReader("sample-unmatched-vehicle.xml");

		Map<String, List<Vehicle>> vehicleTypeWithCount = VehicleIdentifier.getVehicleTypeWithCount(xmlStreamReader);

		assertTrue(vehicleTypeWithCount.isEmpty());
	}

	@Test
	public void shouldNotReturnTypeWithCountOfVehiclesIfNoVehicleTagFound() throws XMLStreamException {
		XMLStreamReader xmlStreamReader = getXmlStreamReader("sample-no-vehicle.xml");

		Map<String, List<Vehicle>> vehicleTypeWithCount = VehicleIdentifier.getVehicleTypeWithCount(xmlStreamReader);

		assertTrue(vehicleTypeWithCount.isEmpty());
	}

	@Test
	public void shouldNotReturnTypeWithCountOfVehiclesIfNoWheelTagFound() throws XMLStreamException {
		XMLStreamReader xmlStreamReader = getXmlStreamReader("sample-no-wheel.xml");

		Map<String, List<Vehicle>> vehicleTypeWithCount = VehicleIdentifier.getVehicleTypeWithCount(xmlStreamReader);

		assertTrue(vehicleTypeWithCount.isEmpty());
	}

	@Test
	public void shouldNotReturnTypeWithCountOfVehiclesIfNoFrameTagFound() throws XMLStreamException {
		XMLStreamReader xmlStreamReader = getXmlStreamReader("sample-no-frame.xml");

		Map<String, List<Vehicle>> vehicleTypeWithCount = VehicleIdentifier.getVehicleTypeWithCount(xmlStreamReader);

		assertTrue(vehicleTypeWithCount.isEmpty());
	}

	@Test
	public void shouldNotReturnTypeWithCountOfVehiclesIfNoPowertrainTagFound() throws XMLStreamException {
		XMLStreamReader xmlStreamReader = getXmlStreamReader("sample-no-powertrain.xml");

		Map<String, List<Vehicle>> vehicleTypeWithCount = VehicleIdentifier.getVehicleTypeWithCount(xmlStreamReader);

		assertTrue(vehicleTypeWithCount.isEmpty());
	}

	@Test
	public void shouldNotReturnTypeWithCountOfVehiclesIfSomeWheelMaterialDoNotMatch() throws XMLStreamException {
		XMLStreamReader xmlStreamReader = getXmlStreamReader("sample-no-wheel-material.xml");

		Map<String, List<Vehicle>> vehicleTypeWithCount = VehicleIdentifier.getVehicleTypeWithCount(xmlStreamReader);

		assertTrue(vehicleTypeWithCount.isEmpty());
	}

	private XMLStreamReader getXmlStreamReader(String xmlFile) throws XMLStreamException {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		return factory.createXMLStreamReader(ClassLoader.getSystemResourceAsStream(xmlFile));
	}
}
