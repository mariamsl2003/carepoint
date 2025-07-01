package com.capstone.demo.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.capstone.demo.Enum.RequestResult;
import com.capstone.demo.Enum.RequestToGet;
import com.capstone.demo.Model.MedicineModel;
import com.capstone.demo.Model.MemberModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.capstone.demo.Model.MedicalModel;

public interface MedicalRepository extends JpaRepository<MedicalModel, UUID> {

    @Query(value = "select * from medical where id = ?1", nativeQuery = true)
    MedicalModel findByMedicalId(Long id);

    @Query(value = "select * from medical where request_result = ?1", nativeQuery = true)
    List<MedicalModel> findByRequestResult(RequestResult request);

    @Query(value = "select count(*) from medical where donor = ?1", nativeQuery = true)
    Long countByDonor(Long id);

    @Query(value = "select count(*) from medical where requester = ?1", nativeQuery = true)
    Long countByRequester(Long id);

    @Query(value = "select * from medical where requester = ?1", nativeQuery = true)
    List<MedicalModel> myRequesting(Long id);

    @Query(value = "select * from medical where donor = ?1", nativeQuery = true)
    List<MedicalModel> myDonating(Long id);

    @Query(value = "select * from medical where requested =?1", nativeQuery = true)
    List<MedicalModel> requestedMedical(RequestToGet request);

}
