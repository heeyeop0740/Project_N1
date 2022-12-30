package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Admin;
import com.example.demo.service.TokenService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "관리자 로그인 API")
@RestController
public class LoginController {

	TokenService tokenService;
	
	public LoginController(TokenService tokenService) {
		this.tokenService = tokenService;
	}
	
	// 토큰 생성
	@SecurityRequirement(name = "login API")
	@PostMapping("/login")
	public String login(Admin user) {
		
		// id 기준으로 pwd 일치 확인
		Admin admin = tokenService.getAdminById(user);
				
		// 일치하면 토큰 리턴
		if (tokenService.validateAdmin(admin, user)) {
			return tokenService.createToken(user);
		}
		
		// 일치하지 않으면 어떤 처리??
		return "invalid";
	}
}
