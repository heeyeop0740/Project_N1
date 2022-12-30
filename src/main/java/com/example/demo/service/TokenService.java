package com.example.demo.service;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.example.demo.model.Admin;
import com.example.demo.repository.AdminMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;

@Service
public class TokenService {
	
	public static String keyString = "We8zEU/7F++IhZ/PLhJWXLRtHrcPLn79UR1Pe1MUmfo="; // DB에서 가져오는것 고려
	
	AdminMapper adminMapper;
	
	public TokenService(AdminMapper adminMapper) {
		
		this.adminMapper = adminMapper;
	}
	
	// 넘어온 id와 pwd를 db에서 조회
	public Admin getAdminById(Admin admin) {
		
		return adminMapper.getAdminById(admin);
	}
	
	// id 기준으로 pwd 일치 확인
	public boolean validateAdmin(Admin admin, Admin user) {
		
		if (admin.getPwd().equals(user.getPwd())) {
			return true;
		}
		return false;
	}
	
	public Key getKey(String keyString) {
		
		byte[] encodedKey = Decoders.BASE64.decode(keyString);
		return new SecretKeySpec(encodedKey, "HmacSHA256");
	}
	
	public String createToken(Admin admin) {

		// key 생성
		Key key = getKey(keyString);
		
		// token에 저장할 내용 생성
		Calendar now = Calendar.getInstance();
		now.add(Calendar.HOUR, 1);
		long exp = now.getTimeInMillis();
		
		// sub, iat, exp, 저장 후 토큰 생성하여 리턴
		return Jwts.builder()
				.setSubject("admin")
				.setExpiration(new Date(exp))
				.signWith(key)
				.compact();
	}
	
	// 유효한 토큰인지 검사
	public boolean validateToken(HttpServletRequest request, Key key) {
		
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String token ="";
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring("Bearer ".length());
		}
				
		Jws<Claims> jws;
		try {
			jws = Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token);
			
			if (jws.getBody().getSubject().equals("admin") && (new Date().compareTo(jws.getBody().getExpiration()) <= 0))
			{
				return true;
			}
			return false;
		}catch (JwtException ex) {
			return false;
		}
	}

}
