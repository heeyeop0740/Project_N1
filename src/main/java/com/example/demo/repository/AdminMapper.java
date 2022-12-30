package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.Admin;

@Mapper
public interface AdminMapper {
	
	public Admin getAdminById(Admin admin);

}
