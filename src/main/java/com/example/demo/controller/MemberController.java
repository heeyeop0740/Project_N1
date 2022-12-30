package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Member;
import com.example.demo.service.MemberService;
import com.example.demo.service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(name = "Member", description = "멤버 관리 API")
@RestController
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private TokenService tokenService;

	@SecurityRequirement(name = "login API")
	@Operation(summary = "멤버 조회", description = "특정 id를 가진 멤버를 조회합니다.")
	@GetMapping("/member/{id}")
	public @ResponseBody Member getMember(@PathVariable int id, HttpServletRequest request) {
		
		if(tokenService.validateToken(request, tokenService.getKey(TokenService.keyString))) {
			return memberService.getMember(id);
		}
		return null;
	}
	
	@SecurityRequirement(name = "login API")
	@Operation(summary = "모든 멤버 조회", description = "멤버 전체를 조회합니다.")
	@GetMapping("/member/all")
	public @ResponseBody Map<String, Object> getMemberList(HttpServletRequest request) {
		
		Map<String, Object> res = new HashMap<>();
		
		if (!tokenService.validateToken(request, tokenService.getKey(TokenService.keyString))) {
			res.put("status", "invalid token");
			return res;
		}
		
		res.put("memberList", memberService.getMemberList());
		res.put("status", "valid");
		return res;
	}
		
	@SecurityRequirement(name = "login API")
	@Operation(summary = "멤버 추가", description = "새로운 멤버를 추가합니다.")
	@PostMapping("/member")
	public Map<String, Object> createMember(@RequestBody Member member, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		
		if(tokenService.validateToken(request, tokenService.getKey(TokenService.keyString))) {
			if (memberService.createMember(member) == 1) {
				result.put("status", HttpStatus.OK);
				result.put("member", member);
				return result;
			}
		}
		result.put("status", HttpStatus.BAD_REQUEST);
		return result;
	}
	
	@SecurityRequirement(name = "login API")
	@Operation(summary = "멤버 수정", description = "특정 id의 멤버의 정보를 수정합니다.")
	@PutMapping("/member")
	public ResponseEntity updateMember(@RequestBody Member member, HttpServletRequest request) {
		
		if (!tokenService.validateToken(request, tokenService.getKey(TokenService.keyString))) {
			return null;
		}
		
		if(memberService.updateMember(member)==1) {
			return new ResponseEntity<>(member, HttpStatus.OK);
		}
		return new ResponseEntity<>(member, HttpStatus.BAD_REQUEST);
	}
	
	@SecurityRequirement(name = "login API")
	@Operation(summary = "멤버 삭제", description = "특정 id를 가진 멤버를 삭제합니다.")
	@DeleteMapping("/member/{id}")
	public ResponseEntity deleteMember(@PathVariable int id, HttpServletRequest request) {
				
		if (!tokenService.validateToken(request, tokenService.getKey(TokenService.keyString))) {
			return null;
		}
		
		if(memberService.deleteMember(id) == 1)
			return new ResponseEntity<>(id, HttpStatus.OK);
		return new ResponseEntity<>(id, HttpStatus.BAD_REQUEST);
	}  
}
