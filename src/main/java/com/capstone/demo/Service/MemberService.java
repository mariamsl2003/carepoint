package com.capstone.demo.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.capstone.demo.Enum.RequestVolunteer;
import com.capstone.demo.Enum.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Repository.MemberRepository;

import javax.xml.crypto.Data;

@Service
public class MemberService {

    private final String uploadDir = "uploaded/member";
    @Autowired
    private MemberRepository memberRepository;

    // create member (test it when done with login)
    public MemberModel createMember(String username, String password, long phoneNumber) {
        MemberModel member = new MemberModel(username, password, phoneNumber, Roles.MEMEBR);
        member = memberRepository.save(member);
        return member;
    }

    // find member by username (test it when done with login)
    public Optional<MemberModel> findMemberByUserName(String username) {
        return memberRepository.findUserByUserName(username);
    }

    // find member by id
    public MemberModel findById(Long id) {
        return memberRepository.findMemberById(id);
    }

    // get all users (test it when done with admin side)
    public List<MemberModel> getAllMembers() {
        return memberRepository.findAll();
    }

    // uploading the image + updating the medicine (test later)
    public void uploadImage(MultipartFile file, UUID id) throws RuntimeException, IOException {
        MemberModel member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        String filePath = uploadDir + file.getOriginalFilename();
        file.transferTo(new File(filePath));

        member.setImagePath(filePath);
        memberRepository.save(member);
    }

    // updating the member
    public MemberModel updateMember(Long id, String username, long phoneNumber, String password) {
        MemberModel member = memberRepository.findMemberById(id);
        member.setPassword(password);
        member.setPhoneNumber(phoneNumber);
        member.setUsername(username);
        memberRepository.save(member);
        return member;
    }

    //get member by its request to be a volunteer
    public List<MemberModel> getPendingMember(){
        return memberRepository.getPendingMember();
    }

    //update the role and volunteering request if accepting
    public void updateRole(Long id){
        MemberModel member = memberRepository.findMemberById(id);
        member.setRole(Roles.PHARMACIST);
        member.setRequest(RequestVolunteer.ACCEPTED);
        memberRepository.save(member);
    }

    //update only volunteering request if rejecting
    public void updateVolunteer(Long id){
        MemberModel member = memberRepository.findMemberById(id);
        member.setRequest(RequestVolunteer.REJECTED);
        memberRepository.save(member);
    }

    //update volunteer to be requested
    public void updatePending(Long id, Date certificantDate, String syndicateId){
        MemberModel member = memberRepository.findMemberById(id);
        member.setRequest(RequestVolunteer.PENDING);
        member.setVolunteerCertificantDate(certificantDate);
        member.setVolunteerSyndicateId(syndicateId);
        memberRepository.save(member);
    }
}
