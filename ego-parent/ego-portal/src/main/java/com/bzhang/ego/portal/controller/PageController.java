package com.bzhang.ego.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class PageController {
	
	@RequestMapping("/")
	public String welcome() {
		System.out.println("I have been used ...");
		return "forward:/showPic";
	}
	
	
}
