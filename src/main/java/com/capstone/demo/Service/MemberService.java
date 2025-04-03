package com.capstone.demo.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.demo.Enum.Volunteering;
import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Repository.MemberRepository;

@Service
public class MemberService {

    private final String uploadDir = "uploaded/member";
    @Autowired
    private MemberRepository memberRepository;

    // create member (test it when done with login)
    public MemberModel createMember(String username, String passsword, long phoneNumber) {
        MemberModel member = new MemberModel(username, passsword, phoneNumber, Volunteering.MEMEBR);
        member = memberRepository.save(member);
        return member;
    }

    // find member by username (test it when done with login)
    public MemberModel findMemberByUserName(String username) {
        return memberRepository.findUserByUserName(username);
    }

    // find member by id
    public MemberModel findById(UUID id) {
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
}
