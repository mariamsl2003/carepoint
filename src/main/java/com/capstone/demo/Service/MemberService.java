package com.capstone.demo.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.capstone.demo.Config.SecurityConfig;
import com.capstone.demo.Enum.RequestVolunteer;
import com.capstone.demo.Enum.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Repository.MemberRepository;

import javax.xml.crypto.Data;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);


    // create member (test it when done with login)
    public MemberModel createMember(String username, String email, String password, String address, long phoneNumber) {
        String encodePassword = passwordEncoder.encode(password);
        MemberModel member = new MemberModel(username, encodePassword, email, Roles.MEMBER);
        member.setAddress(address);
        member.setPhoneNumber(phoneNumber);
        member = memberRepository.save(member);
        return member;
    }

    // find member by username (test it when done with login)
    public MemberModel findMemberByUserName(String username) {
        return memberRepository.findUserByUserName(username);
    }

    public Optional<MemberModel> findMemberByEmail(String email){
        logger.info("email in service is: {}", email);
        return memberRepository.findMemberByEmail(email);
    }

    public MemberModel findUsingEmail(String email){
        return memberRepository.findUsingEmail(email);
    }

    // find member by id
    public MemberModel findById(Long id) {
        return memberRepository.findMemberById(id);
    }

    // get all users (test it when done with admin side)
    public List<MemberModel> getAllMembers() {
        return memberRepository.findAll();
    }


    // updating the member
    public MemberModel updateMember(Long id, String email, long phoneNumber, String password, String address) {
        MemberModel member = memberRepository.findMemberById(id);
        String encodePassword = passwordEncoder.encode(password);
        member.setPassword(encodePassword);
        member.setPhoneNumber(phoneNumber);
        member.setAddress(address);
        member.setEmail(email);
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
    public void updatePending(Long id, Long licenseNumber, String currentWork){
        MemberModel member = memberRepository.findMemberById(id);
        member.setRequest(RequestVolunteer.PENDING);
        member.setLicenseNumber(licenseNumber);
        member.setCurrentWork(currentWork);
        memberRepository.save(member);
    }


}
