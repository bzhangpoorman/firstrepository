package com.bzhang.service;

import java.util.List;

import com.bzhang.pojo.Airport;

public interface AirportService {
	List<Airport> showTakePort();
	List<Airport> showLandPort();
} 
