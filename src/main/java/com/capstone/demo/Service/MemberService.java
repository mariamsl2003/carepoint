package com.capstone.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.demo.Enum.Volunteering;
import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Repository.MemberRepository;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    // create member (test it when done with login)
    public MemberModel createMember(String username, String passsword, long phoneNumber) {
        MemberModel member = new MemberModel(username, passsword, phoneNumber, Volunteering.MEMEBR);
        member = memberRepository.save(member);
        return member;
    }

    // find member by username (test it when done with login)
    public Optional<MemberModel> findMemberByUserName(String username) {
        return memberRepository.findUserByUserName(username);
    }

    // get all users (test it when done with admin side)
    public List<MemberModel> getAllMembers() {
        return memberRepository.findAll();
    }
}
