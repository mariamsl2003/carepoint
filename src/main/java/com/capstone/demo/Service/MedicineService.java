package com.capstone.demo.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.demo.Model.MedicineModel;
import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Repository.MedicineRepository;

@Service
public class MedicineService {

    private final String uploadDir = "uploaded/medicine";
    @Autowired
    private MedicineRepository medicineRepository;

    // create new medicine (test it when finsih the profile)
    public MedicineModel createMedicine(String name, String description, MemberModel member) {
        MedicineModel medicine = new MedicineModel(name, description, member);
        medicine = medicineRepository.save(medicine);
        return medicine;
    }

    // get all medicine (will be tested soon)
    public List<MedicineModel> getAllMedicine() {
        return medicineRepository.findAll();
    }

    // get medicine by member (test it when finish the profile)
    public Optional<List<MedicineModel>> getMedicineByMember(UUID id) {
        return medicineRepository.findByMemberId(id);
    }

    // get medicne by requestResult (test it when finish with admin side)
    public Optional<List<MedicineModel>> getMedicineAccepted() {
        return medicineRepository.findByRequestResult();
    }

    // get medicine byrequestResult (test it when finsish the doc volunteer profile)
    public Optional<List<MedicineModel>> getMedicinePending() {
        return medicineRepository.findByRequestResultPending();
    }

    // uploading the image + updating the medicine (test later)
    public void uploadImage(MultipartFile file, UUID id) throws RuntimeException, IOException {
        MedicineModel medicine = medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found"));

        String filePath = uploadDir + file.getOriginalFilename();
        file.transferTo(new File(filePath));

        medicine.setImagePath(filePath);
        medicineRepository.save(medicine);
    }

    // finding the medicine by it's name
    public Optional<List<MedicineModel>> findMedicineByName(String name) {
        return medicineRepository.findByName(name);
    }

}
