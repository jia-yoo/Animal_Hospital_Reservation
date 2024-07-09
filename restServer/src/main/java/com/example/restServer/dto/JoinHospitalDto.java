package com.example.restServer.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JoinHospitalDto {
	private Long id;
	//로그인 테이블
	private String username;
	private String password;
	//의사 테이블
    private String doctorNames;
    //멤버 테이블
    private String address;
    private String phone;
    private String businessNumber;
    private String hospitalName;
    private String representative;
    private String businessHours;
    private Boolean partnership;
    private String introduction;
    private String role;
    private String logo;
    private String token;
    private String status;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
//멤버,의사,로그인 테이블 다 넣어야함@@@@@