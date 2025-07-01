package com.capstone.demo.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.capstone.demo.Enum.RequestResult;
import com.capstone.demo.Enum.RequestToGet;
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
    public MedicineModel createMedicine(String name, long quantity, MultipartFile item_image, MultipartFile date_image,String description, MemberModel donor)
            throws IOException {
        MedicineModel medicine = new MedicineModel(name, quantity, donor, RequestResult.PENDING);
        medicine = uploadImage(item_image, medicine, "item");
        medicine = uploadImage(date_image, medicine, "date");
        medicine.setDescription(description);
        medicine = medicineRepository.save(medicine);
        return medicine;
    }

    // get all medicine (will be tested soon)
    public List<MedicineModel> getAllMedicine() {
        return medicineRepository.findAll();
    }

    // get medicine by requestResult
    public List<MedicineModel> getMedicinePending() {
        return medicineRepository.findByRequestResult(RequestResult.PENDING);
    }

    //get accepted donation
    public List<MedicineModel> getAcceptedDonation(){
        return medicineRepository.findByRequestResult(RequestResult.ACCEPTED);
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
        else if(what.equals("date")){
            medicine.setDate_image(filePath);
        }
        else if(what.equals("prescript")){
            medicine.setPrescript(filePath);
        }
        return medicine;
    }

    //count the number of medicine a donor have
    public Long getCountByDonor(Long id){
        return medicineRepository.countByDonor(id);
    }

    //count the number of medicine a requester have
    public Long getCountByRequester(Long id){
        return medicineRepository.countByRequester(id);
    }

    //get all the requests of a member
    public List<MedicineModel> myRequests(Long id){
        return medicineRepository.myRequesting(id);
    }

    //get all the donation of a member
    public List<MedicineModel> myDonations(Long id){
        return medicineRepository.myDonating(id);
    }

    //get the requested medicines to be get
    public List<MedicineModel> requestMedicineRequested(){
        return medicineRepository.requestedMedicine(RequestToGet.REQUESTED);
    }

    public void requesting(Long id, Long quantity, MultipartFile prescript, MemberModel requester) throws IOException {
        MedicineModel medicine = medicineRepository.findById(id);
        medicine.setQuantity_needed(quantity);
        medicine = uploadImage(prescript, medicine,"prescript");
        medicine.setRequester(requester);
        medicine.setRequested(RequestToGet.REQUESTED);
        medicineRepository.save(medicine);
    }

    //accept the donation
    public void acceptDonation(Long id){
        MedicineModel medicine = medicineRepository.findById(id);
        medicine.setRequestResult(RequestResult.ACCEPTED);
        medicineRepository.save(medicine);
    }

    //reject the donation
    public void rejectDonation(Long id){
        MedicineModel medicine = medicineRepository.findById(id);
        medicine.setRequestResult(RequestResult.REJECTED);
        medicineRepository.save(medicine);
    }

    //accept the request
    public void acceptRequest(Long id){
        MedicineModel medicine = medicineRepository.findById(id);
        medicine.setRequested(RequestToGet.ACCEPTED);
        medicineRepository.save(medicine);
    }

    //reject the request
    public void rejectRequest(Long id){
        MedicineModel medicine = medicineRepository.findById(id);
        medicine.setRequested(RequestToGet.REJECTED);
        medicineRepository.save(medicine);
    }
}

