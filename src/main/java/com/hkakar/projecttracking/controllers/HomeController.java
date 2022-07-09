package com.hkakar.projecttracking.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@RequestMapping("/")
	public String getHome() {
		return "HELLO-WORLD";
	}
}
