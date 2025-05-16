package com.ahmetarabaci.ssoclientservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/sso")
@SecurityRequirement(name = "Keycloak")
public class SSOClientController {

	@GetMapping("/testaccess")
	public String testSSOAccess() {
		return "'/sso/testaccess' endpoint has been executed successfully!";
	}
}

