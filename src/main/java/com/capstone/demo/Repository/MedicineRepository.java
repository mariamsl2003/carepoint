package com.capstone.demo.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.capstone.demo.Model.MedicineModel;

public interface MedicineRepository extends JpaRepository<MedicineModel, UUID> {

    @Query(value = "select * from medicine where member_id = ?1", nativeQuery = true)
    Optional<List<MedicineModel>> findByMemberId(UUID id);

    @Query(value = "select * from medicine where requestResult = 'ACCEPTED'", nativeQuery = true)
    Optional<List<MedicineModel>> findByRequestResult();

    @Query(value = "select * from medicine where requestResult = 'PENDING'", nativeQuery = true)
    List<MedicineModel> findByRequestResultPending();

    @Query(value = "select * from medicine where name = ?1", nativeQuery = true)
    Optional<List<MedicineModel>> findByName(String name);

    @Query(value = "select * from medicine order by random() limit 3", nativeQuery = true)
    List<MedicineModel> findThreeRandomMedicine();

    @Query(value = "select * from medicine where requested = 'REQUESTED'", nativeQuery = true)
    List<MedicineModel> findMedicinesByRequested();

}
