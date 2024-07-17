package com.example.restServer.service.user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.restServer.dto.MemVetDto;
import com.example.restServer.entity.Bookmark;
import com.example.restServer.entity.Member;
import com.example.restServer.repository.BookmarkRepository;
import com.example.restServer.repository.MemberRepository;
import com.example.restServer.repository.ReservationRepository;
import com.example.restServer.util.DateTimeUtil;

@Service
public class VetListService {

    @Autowired 
    private MemberRepository memRepo;
    @Autowired 
    private ReservationRepository reserveRepo;
    @Autowired 
    private BookmarkRepository bookmarkRepo;

    public List<MemVetDto> getMemberVetList(String address, Long userId) {
        List<Member> result = memRepo.findMemberVetList(address);
        return createMemVetDtoList(result, userId);
    }

    public String isBookmarked(Long hosId, Long userId, Boolean isBookmarked) {
        if (bookmarkRepo.isBookmarked(hosId, userId).isEmpty()) {
            Bookmark newBookmark = new Bookmark();
            newBookmark.setUser(memRepo.findById(userId).get());
            newBookmark.setHospital(memRepo.findById(hosId).get());
            bookmarkRepo.save(newBookmark);
        } else {
            Bookmark oldBookmark = bookmarkRepo.isBookmarked(hosId, userId).get();
            bookmarkRepo.delete(oldBookmark);
        }
        return "";
    }

    public List<MemVetDto> getMemberVet(String hospitalName, String address, Long userId) {
        List<Member> result = memRepo.findMemberVet(address, hospitalName);
        return createMemVetDtoList(result, userId);
    }

    public MemVetDto getVetDetail(Long hosId, Long userId) {
        Member hospital = memRepo.findById(hosId).orElse(null);
        if (hospital == null) {
            return null;
        }
        MemVetDto mv = createMemVetDto(hospital, userId);
        return mv;
    }

    private List<MemVetDto> createMemVetDtoList(List<Member> members, Long userId) {
        List<MemVetDto> memVetList = new ArrayList<>();
        for (Member hospital : members) {
            memVetList.add(createMemVetDto(hospital, userId));
        }
        return memVetList;
    }

    private MemVetDto createMemVetDto(Member hospital, Long userId) {
        MemVetDto mv = new MemVetDto();
        Long hospitalId = hospital.getId();

        mv.setId(hospitalId);
        mv.setAddress(hospital.getAddress());
        mv.setPhone(hospital.getPhone());
        mv.setHospitalName(hospital.getHospitalName());
        mv.setRepresentative(hospital.getRepresentative());
        mv.setBusinessHours(DateTimeUtil.getBusinessHours(hospital.getBusinessHours()));
        mv.setBusinessNumber(hospital.getBusinessNumber());
        mv.setIntroduction(hospital.getIntroduction());
        mv.setPartnership(hospital.getPartnership());
        mv.setLogo(hospital.getLogo());
        mv.setEmail(hospital.getEmail());
        mv.setAvgReview(reserveRepo.findAvgReview(hospitalId));
        mv.setReview(reserveRepo.findReservWithReview(hospitalId));
        mv.setBookmarked(userId != 0L && !bookmarkRepo.isBookmarked(hospitalId, userId).isEmpty());

        return mv;
    }
}
