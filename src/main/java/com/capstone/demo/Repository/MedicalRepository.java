package com.capstone.demo.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.capstone.demo.Model.MedicalModel;

public interface MedicalRepository extends JpaRepository<MedicalModel, UUID> {

    @Query(value = "select * from medical where member_id = ?1", nativeQuery = true)
    Optional<List<MedicalModel>> findByMemberId(UUID id);

    @Query(value = "select * from medical where requestResult = 'ACCEPTED'", nativeQuery = true)
    Optional<List<MedicalModel>> findByRequestResult();

    @Query(value = "select * from medical where requestResult = 'PENDING'", nativeQuery = true)
    Optional<List<MedicalModel>> findByRequestResultPending();

    @Query(value = "select * from medical where name = ?1", nativeQuery = true)
    Optional<List<MedicalModel>> findByName(String name);

    @Query(value = "select * from medical order by random() limit 3", nativeQuery = true)
    List<MedicalModel> findThreeRandomMedical();

}
