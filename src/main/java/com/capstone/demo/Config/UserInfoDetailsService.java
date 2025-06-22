package com.capstone.demo.Config;

import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Configuration
public class UserInfoDetailsService implements UserDetailsService {
    @Autowired
    private MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MemberModel> member = memberService.findMemberByUserName(username);
        return member.map(UserInfoDetails::new).orElseThrow(()-> new UsernameNotFoundException("this username is not found in the database"));
    }
}
