package com.capstone.demo.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.capstone.demo.Model.MemberModel;

public interface MemberRepository extends JpaRepository<MemberModel, UUID> {
    @Query(value = "select * from member where username = ?1", nativeQuery = true)
    MemberModel findUserByUserName(String name);

    @Query(value = "select * from member where id = ?1", nativeQuery = true)
    MemberModel findMemberById(UUID id);

}
