package br.com.authentication.controllers;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class ViewController {

	@GetMapping("/login")
	public String login() {
		return "login";
	}

}
