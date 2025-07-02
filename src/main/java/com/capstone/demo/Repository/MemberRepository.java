package com.capstone.demo.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.capstone.demo.Model.MemberModel;

public interface MemberRepository extends JpaRepository<MemberModel, UUID> {
    @Query(value = "select * from member where username = ?1", nativeQuery = true)
    MemberModel findUserByUserName(String name);

    @Query(value = "select * from member where id = ?1", nativeQuery = true)
    MemberModel findMemberById(Long id);

    @Query(value = "select * from member where request = 'PENDING'", nativeQuery = true)
    List<MemberModel> getPendingMember();

    @Query(value = "select * from member where email = ?1", nativeQuery = true)
    Optional<MemberModel> findMemberByEmail(String email);

    @Query(value = "select * from member where email = ?1", nativeQuery = true)
    MemberModel findUsingEmail(String email);

}
