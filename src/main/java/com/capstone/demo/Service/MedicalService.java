package com.capstone.demo.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.demo.Model.MedicalModel;
import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Repository.MedicalRepository;

@Service
public class MedicalService {

    private final String uploadDir = "uploaded/medical";
    @Autowired
    private MedicalRepository medicalRepository;

    // create new medical (test it when finsih the profile)
    public MedicalModel createMedical(String name, String description, MemberModel member, MultipartFile image)
            throws IOException {
        MedicalModel medical = new MedicalModel(name, description, member);
        uploadImage(image, medical);
        medical = medicalRepository.save(medical);
        return medical;
    }

    // get all medical (will be tested soon)
    public List<MedicalModel> getAllMedical() {
        return medicalRepository.findAll();
    }

    // get medical by member (test it when finish the profile)
    public Optional<List<MedicalModel>> getMedicalByMember(UUID id) {
        return medicalRepository.findByMemberId(id);
    }

    // get medical by requestResult (test it when finish with admin side)
    public Optional<List<MedicalModel>> getMedicalAccepted() {
        return medicalRepository.findByRequestResult();
    }

    // get medical byrequestResult (test it when finsish the doc volunteer profile)
    public Optional<List<MedicalModel>> getMedicalPending() {
        return medicalRepository.findByRequestResultPending();
    }

    // uploading the image + updating the medical (test later)
    public MedicalModel uploadImage(MultipartFile file, MedicalModel medical) throws IOException {
        String filePath = uploadDir + file.getOriginalFilename();
        file.transferTo(new File(filePath));

        medical.setImagePath(filePath);
        return medical;
    }

    // finding the medical by it's name
    public Optional<List<MedicalModel>> findMedicalByName(String name) {
        return medicalRepository.findByName(name);
    }

    // returning three random medical
    public List<MedicalModel> getThreeRandomMedical() {
        return medicalRepository.findThreeRandomMedical();
    }

    // get the medical who is requested by the member
    public List<MedicalModel> getRequestedMedical(MemberModel curentMember) {
        List<MedicalModel> medicals = medicalRepository.findMedicalsByRequested();
        List<MedicalModel> medicalsRequested = new ArrayList<>();
        int i = 0;
        while (i <= medicals.size()) {
            if (medicals.get(i).getMemberToGet() == curentMember) {
                medicalsRequested.add(medicals.get(i));
            }
            i++;
        }
        return medicalsRequested;
    }

}
