package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberMapper;

@Service
public class MemberService {
	
//	@Autowired
	MemberMapper memberMapper;
	
	public MemberService(MemberMapper memberMapper) {
		this.memberMapper = memberMapper;
	}
	
	public List<Member> getMemberList() {
		
		return memberMapper.getMemberList();
	}
	
	public Member getMember(int id) {
		return memberMapper.getMember(id);
	}
	
	public int createMember(Member member) {
		return memberMapper.createMember(member);
	}
	
	public int updateMember(Member member) {
		return memberMapper.updateMember(member);
	}
	
	public int deleteMember(int id) {
		return memberMapper.deleteMember(id);
	}
}
