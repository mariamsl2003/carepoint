package com.capstone.demo.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.capstone.demo.Enum.RequestResult;
import com.capstone.demo.Enum.RequestToGet;
import com.capstone.demo.Model.MedicalModel;
import com.capstone.demo.Model.MemberModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.capstone.demo.Model.MedicineModel;

public interface MedicineRepository extends JpaRepository<MedicineModel, UUID> {

    @Query(value = "select * from medicine where id = ?1", nativeQuery = true)
    MedicineModel findById(Long id);

    @Query(value = "select * from medicine where request_result = ?1", nativeQuery = true)
    List<MedicineModel> findByRequestResult(RequestResult request);


    @Query(value = "select count(*) from medicine where donor = ?1", nativeQuery = true)
    Long countByDonor(Long id);

    @Query(value = "select count(*) from medicine where requester = ?1", nativeQuery = true)
    Long countByRequester(Long id);

    @Query(value = "select * from medicine where requester = ?1", nativeQuery = true)
    List<MedicineModel> myRequesting(Long id);

    @Query(value = "select * from medicine where donor = ?1", nativeQuery = true)
    List<MedicineModel> myDonating(Long id);

    @Query(value = "select * from medicine where requested =?1", nativeQuery = true)
    List<MedicineModel> requestedMedicine(RequestToGet request);

}
