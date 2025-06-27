package com.capstone.demo.Config;

import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(UserInfoDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<MemberModel> member = memberService.findMemberByEmail(email);
        logger.info("email in the userInfoDetailsService is {}", email);
        return member.map(UserInfoDetails::new).orElseThrow(()-> new UsernameNotFoundException("this email is not found in the database"));
    }
}
