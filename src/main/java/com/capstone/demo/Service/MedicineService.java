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

    //get All Donation
    public List<MedicineModel> getAllDonation(){
        return medicineRepository.getAllDonation();
    }

    //get All Request
    public List<MedicineModel> getAllRequests(){
        return medicineRepository.getAllRequests();
    }

    //update the medicine
    public void updateMedicine(MedicineModel medicine, MultipartFile itemImage, MultipartFile dateImage) throws IOException {
        medicine = uploadImage(itemImage, medicine, "item");
        medicine = uploadImage(dateImage, medicine, "date");
        medicineRepository.save(medicine);
    }

    // get medicine by requestResult
    public List<MedicineModel> getMedicinePending() {
        return medicineRepository.findByRequestResult(RequestResult.PENDING.name());
    }

    //get accepted donation
    public List<MedicineModel> getAcceptedDonation(){
        System.out.println("RequestResult.ACCEPTED: " + RequestResult.ACCEPTED);
        return medicineRepository.findByRequestResult(RequestResult.ACCEPTED.name());
    }

    // uploading the image + updating the medicine (test later)
    public MedicineModel uploadImage(MultipartFile file, MedicineModel medicine, String what) throws IOException {
        // Ensure the upload directory exists
        File uploadDirFile = new File("src/main/resources/static/uploaded"); // Set the correct upload directory
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }
        // Construct the file path
        String filePath = uploadDirFile.getAbsolutePath() + File.separator + file.getOriginalFilename(); // Use File.separator for OS compatibility
        // Save the file
        file.transferTo(new File(filePath));

        // Update the medicine with the relative URL
        String relativeUrl = "/uploaded/" + file.getOriginalFilename();
        //update the medicine
        if(what.equals("item")){
            medicine.setItem_image(relativeUrl);
        }
        else if(what.equals("date")){
            medicine.setDate_image(relativeUrl);
        }
        else if(what.equals("prescript")){
            medicine.setPrescript(relativeUrl);
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

    //remove donated medicine
    public void removeMedicine(Long id){
        MedicineModel medicine = medicineRepository.findById(id);
        medicine.setRequester(null);
        medicineRepository.removeById(id);
    }

    //remove requested medicine
    public void removeRequestingMedicine(Long id){
        MedicineModel medicine = medicineRepository.findById(id);
        medicine.setRequested(null);
        medicine.setRequester(null);
        medicineRepository.save(medicine);
    }

    //get all the donation of a member
    public List<MedicineModel> myDonations(Long id){
        return medicineRepository.myDonating(id);
    }

    //get the requested medicines to be get
    public List<MedicineModel> requestMedicineRequested(){
        return medicineRepository.requestedMedicine(RequestToGet.REQUESTED.name());
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

