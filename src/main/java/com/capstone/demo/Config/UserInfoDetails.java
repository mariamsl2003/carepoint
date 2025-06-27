package com.capstone.demo.Config;

import com.capstone.demo.Model.MemberModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserInfoDetails implements UserDetails {

    private String username;
    private String password;
    private Long memberId;
    private List<GrantedAuthority> roles;
    private static final Logger logger = LoggerFactory.getLogger(UserInfoDetails.class);

    public UserInfoDetails(MemberModel user) {
        logger.info("the emails in the constructor of userInfoDetails is {}", user.getEmail());
        logger.info("the role in the constuctor is {}", user.getRole());
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.memberId = user.getId();
        this.roles = List.of(new SimpleGrantedAuthority(user.getRole().name()));

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        logger.info("the role in the getAuthorities is {}", this.roles);
        return roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public Long getMemberId() {
        return memberId;
    }

    @Override
    public String getUsername() {
        logger.info("the email in getUsername method inside the userInfoDetails class is: {}", this.username);
        logger.info("the role in getUsername is {}", this.roles);
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
}







}
