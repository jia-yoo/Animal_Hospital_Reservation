package com.example.restServer.controller.admin;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restServer.dto.IMemberLoginDto;
import com.example.restServer.entity.Member;
import com.example.restServer.entity.Point;
import com.example.restServer.repository.LoginRepository;
import com.example.restServer.repository.MemberRepository;
import com.example.restServer.repository.PointRepository;
import com.example.restServer.utill.MailService;

import jakarta.annotation.Resource;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/manager")
public class AdminController_jisun {
	
	@Autowired
	MemberRepository memberRepo;
	
	@Autowired
	LoginRepository loginRepo;
	
	@Autowired
	PointRepository pointRepo;
	
	@Resource(name = "mailService")
	private MailService mailService;

	@GetMapping("/permit")
	public ResponseEntity<List<IMemberLoginDto>> getHospitalNonePermit(){
		List<IMemberLoginDto> list = memberRepo.findByStatusWaiting();
		System.out.println(list.toString());
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@PutMapping("/permit/{id}")
	public ResponseEntity<Member> updatePermitOk(@PathVariable("id") Long id){
		Optional<Member> result = memberRepo.findById(id);
		Member member = result.get();
		member.setStatus("승인");
		memberRepo.save(member);
		return new ResponseEntity<>(member, HttpStatus.OK);
	}
	
	@DeleteMapping("/permit/{id}")
	public ResponseEntity<String> deletePermitOk(@PathVariable("id") Long id){
		Optional<Member> result = memberRepo.findById(id);
		Member member = result.get();
		if(member.getEmail() != null) {
			mailService.sendSimpleEmail(member.getEmail());
		}else {
			System.out.println("이메일이 없네용");
		}
		
		loginRepo.deleteByMemberId(id);
		memberRepo.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/user")
	public ResponseEntity<List<IMemberLoginDto>> getUserList(){
		List<IMemberLoginDto> list = memberRepo.findAllAddUsername();
		System.out.println(list.toString());
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	@GetMapping("/user/{id}")
	public ResponseEntity<IMemberLoginDto> getUser(@PathVariable("id")Long id){
		IMemberLoginDto member = memberRepo.findByIdAddUsername(id);
		return new ResponseEntity<>(member, HttpStatus.OK);
	}
	
	
	@GetMapping("/point/{id}")
	public ResponseEntity<List<Point>> getUserPoint(@PathVariable("id")Long id){
		List<Point> pointList = pointRepo.findAllByUserId(id);
		return new ResponseEntity<>(pointList, HttpStatus.OK);
	}
	
	@GetMapping("/point/total/{userId}")
	public ResponseEntity<Integer> getUserPointTotal(@PathVariable("userId")Long userId){
		Integer num = pointRepo.findByUserIdRemainingPoints(userId);
		return new ResponseEntity<>(num, HttpStatus.OK);
	}
	
}