package com.capstone.demo.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.capstone.demo.Enum.RequestResult;
import com.capstone.demo.Enum.RequestToGet;
import com.capstone.demo.Model.MedicineModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.demo.Model.MedicalModel;
import com.capstone.demo.Model.MemberModel;
import com.capstone.demo.Repository.MedicalRepository;

@Service
public class MedicalService {

    private final String uploadDir = "C:\\Users\\Lenovo\\Documents\\Mariam Folder\\Capstone\\carepoint\\project\\demo\\uploaded";
    @Autowired
    private MedicalRepository medicalRepository;

    // create new medical (test it when finsih the profile)
    public MedicalModel createMedical(String name, long quantity, MultipartFile item_image, MultipartFile date_image, String description, MemberModel donor)
            throws IOException {
        MedicalModel medical = new MedicalModel(name, quantity, donor, RequestResult.PENDING);
        uploadImage(item_image, medical, "item");
        uploadImage(date_image, medical, "date");
        medical.setDescription(description);
        medical = medicalRepository.save(medical);
        return medical;
    }

    // get all medical (will be tested soon)
    public List<MedicalModel> getAllMedical() {
        return medicalRepository.findAll();
    }

    // get medical by requestResult (test it when finish the doc volunteer profile)
    public List<MedicalModel> getMedicalPending() {
        return medicalRepository.findByRequestResult(RequestResult.PENDING);
    }

    // uploading the image + updating the medical (test later)
    public MedicalModel uploadImage(MultipartFile file, MedicalModel medical, String what) throws IOException {
        //ensure the upload directory exists
        File uploadDirFile = new File(uploadDir);
        if(!uploadDirFile.exists()){
            uploadDirFile.mkdirs();
        }

        String filePath = uploadDir + file.getOriginalFilename();
        file.transferTo(new File(filePath));

        if(what.equals("item")) {
            medical.setItem_image(filePath);
        }
        else if(what.equals("date")){
            medical.setDate_image(filePath);
        }
        else if(what.equals("prescript")){
            medical.setPrescript(filePath);
        }
        return medical;
    }

    //request form handling
    public void requesting(Long id, Long quantity, MultipartFile prescript, MemberModel requester) throws IOException {
        MedicalModel medical = medicalRepository.findByMedicalId(id);
        medical.setQuantity_needed(quantity);
        medical = uploadImage(prescript, medical, "prescript");
        medical.setRequester(requester);
        medical.setRequested(RequestToGet.REQUESTED);
        medicalRepository.save(medical);
    }

    //count the number of medicine a donor have
    public Long getCountByDonor(Long id){
        return medicalRepository.countByDonor(id);
    }

    //count the number of medicine a requester have
    public Long getCountByRequester(Long id){
        return medicalRepository.countByRequester(id);
    }

    //get all the requests of a member
    public List<MedicalModel> myRequests(Long id){
        return medicalRepository.myRequesting(id);
    }
    //get all the donation of a member
    public List<MedicalModel> myDonations(Long id){
        return medicalRepository.myDonating(id);
    }

    //get the requested medicals to be get
    public List<MedicalModel> requestMedicalRequested(){
        return medicalRepository.requestedMedical(RequestToGet.REQUESTED);
    }

    //get the accepted donations
    public List<MedicalModel> getAcceptedDonation(){
        return medicalRepository.findByRequestResult(RequestResult.ACCEPTED);
    }

    //get medical by id
    public MedicalModel getMedicalById(Long id){
        return medicalRepository.findByMedicalId(id);
    }

    //accept donation
    public void acceptDonation(Long id){
        MedicalModel medical = medicalRepository.findByMedicalId(id);
        medical.setRequestResult(RequestResult.ACCEPTED);
        medicalRepository.save(medical);
    }

    //reject donation
    public void rejectDonation(Long id){
        MedicalModel medical = medicalRepository.findByMedicalId(id);
        medical.setRequestResult(RequestResult.REJECTED);
        medicalRepository.save(medical);
    }

    //accept request
    public void acceptRequest(Long id){
        MedicalModel medical = medicalRepository.findByMedicalId(id);
        medical.setRequested(RequestToGet.ACCEPTED);
        medicalRepository.save(medical);
    }

    //reject request
    public void rejectRequest(Long id){
        MedicalModel medical = medicalRepository.findByMedicalId(id);
        medical.setRequested(RequestToGet.REJECTED);
        medicalRepository.save(medical);
    }
}
