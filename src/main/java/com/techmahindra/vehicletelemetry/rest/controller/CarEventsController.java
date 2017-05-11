package com.techmahindra.vehicletelemetry.rest.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techmahindra.vehicletelemetry.rest.bean.CarAlert;
import com.techmahindra.vehicletelemetry.rest.bean.CarData;
import com.techmahindra.vehicletelemetry.rest.service.CarEventsService;

@RestController
public class CarEventsController {

	CarEventsService CarEventService = new CarEventsService();

	@RequestMapping(value = "/aggressdrivers", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<Integer> getAggressiveDrivers() throws SQLException {
		 return CarEventService.getAggressiveDrivers();
	}
	
	@RequestMapping(value = "/aggdriversmodel", method = RequestMethod.GET, headers = "Accept=application/json")
	public Set<String> getAggressiveDriversModal() throws SQLException {
		 return CarEventService.getAggressiveDriversModal();
	}
	
	@RequestMapping(value = "/aggdriverscity", method = RequestMethod.GET, headers = "Accept=application/json")
	public Set<String> getAggressiveDriversCity() throws SQLException {
		 return CarEventService.getAggressiveDriversCity();
	}
	
	
	@RequestMapping(value = "/fueleffdrivers", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<CarData> getCountries() throws SQLException {
		return CarEventService.getFuelEffcntDrivers();
	}
	
	@RequestMapping(value = "/caralerts", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<CarAlert> getCarAlerts() throws SQLException {
		return CarEventService.getCarAlerts();
	}
	
	/*@RequestMapping(value = "/CarEvent/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public CarEvent getCarEventById(@PathVariable int id) {
		return CarEventService.getCarEvent(id);
	}

	@RequestMapping(value = "/countries", method = RequestMethod.POST, headers = "Accept=application/json")
	public CarEvent addCarEvent(@RequestBody CarEvent CarEvent) {
		return CarEventService.addCarEvent(CarEvent);
	}

	@RequestMapping(value = "/countries", method = RequestMethod.PUT, headers = "Accept=application/json")
	public CarEvent updateCarEvent(@RequestBody CarEvent CarEvent) {
		return CarEventService.updateCarEvent(CarEvent);

	}

	@RequestMapping(value = "/CarEvent/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public void deleteCarEvent(@PathVariable("id") int id) {
		CarEventService.deleteCarEvent(id);

	}*/	
}
