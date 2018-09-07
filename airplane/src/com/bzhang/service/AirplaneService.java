package com.bzhang.service;

import java.util.List;

import com.bzhang.pojo.Airplane;
import com.bzhang.pojo.Airport;

public interface AirplaneService {
	List<Airplane> show(int takeid, int landid);
	
} 
