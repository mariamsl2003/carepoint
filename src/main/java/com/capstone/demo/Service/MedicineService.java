package com.capstone.demo.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.capstone.demo.Enum.RequestResult;
import com.capstone.demo.Model.MedicalModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.demo.Model.MedicineModel;
import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Repository.MedicineRepository;

@Service
public class MedicineService {

    private final String uploadDir = "C:\\Users\\Lenovo\\Documents\\Mariam Folder\\Capstone\\carepoint\\project\\demo\\uploaded";
    @Autowired
    private MedicineRepository medicineRepository;

    // create new medicine (test it when finsih the profile)
    public MedicineModel createMedicine(String name, long quantity, MultipartFile item_image, MultipartFile date_image, MemberModel donor)
            throws IOException {
        MedicineModel medicine = new MedicineModel(name, quantity, donor, RequestResult.PENDING);
        medicine = uploadImage(item_image, medicine, "item");
        medicine = uploadImage(date_image, medicine, "date");
        medicine = medicineRepository.save(medicine);
        return medicine;
    }

    // get all medicine (will be tested soon)
    public List<MedicineModel> getAllMedicine() {
        return medicineRepository.findAll();
    }

    // get medicine by member (test it when finish the profile)
    public Optional<List<MedicineModel>> getMedicineByMember(Long id) {
        return medicineRepository.findByMemberId(id);
    }

    // get medicne by requestResult (test it when finish with admin side)
    public Optional<List<MedicineModel>> getMedicineAccepted() {
        return medicineRepository.findByRequestResult();
    }

    // get medicine by requestResult
    public List<MedicineModel> getMedicinePending() {
        return medicineRepository.findByRequestResultPending();
    }

    // uploading the image + updating the medicine (test later)
    public MedicineModel uploadImage(MultipartFile file, MedicineModel medicine, String what) throws IOException {
        //ensure the upload directory exists
        File uploadDirFile = new File(uploadDir);
        if(!uploadDirFile.exists()){
            uploadDirFile.mkdirs();
        }
        //construct the file path
        String filePath = uploadDir + file.getOriginalFilename();
        // save the file
        file.transferTo(new File(filePath));
        //update the medicine
        if(what.equals("item")){
            medicine.setItem_image(filePath);
        }
        else{
            medicine.setDate_image(filePath);
        }
        return medicine;
    }

    // finding the medicine by it's name
    public Optional<List<MedicineModel>> findMedicineByName(String name) {
        return medicineRepository.findByName(name);
    }

    // returning three random medicine
    public List<MedicineModel> getThreeRandomMedicine() {
        return medicineRepository.findThreeRandomMedicine();
    }

    // get the medicine who is requested by the member
    public List<MedicineModel> getRequestedMedicine(MemberModel curentMember) {
        List<MedicineModel> medicines = medicineRepository.findMedicinesByRequested();
        List<MedicineModel> medicinesRequested = new ArrayList<>();
        int i = 0;
        while (i <= medicines.size()) {
            if (medicines.get(i).getRequester() == curentMember) {
                medicinesRequested.add(medicines.get(i));
            }
            i++;
        }
        return medicinesRequested;
    }

    //find the medicine according to its id
    public Optional<MedicineModel> getMedicineById(Long id){
        Optional<MedicineModel> medicine = medicineRepository.findMedicineByID(id);
        return medicine;
    }
}
