package com.capstone.demo.Model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.capstone.demo.Enum.RequestVolunteer;
import com.capstone.demo.Enum.Roles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "member")
public class MemberModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    private long phoneNumber;

    private String address;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Enumerated(EnumType.STRING)
    private RequestVolunteer request;

    @OneToMany(mappedBy = "donor")
    private List<MedicineModel> medicines;

    @OneToMany(mappedBy = "donor")
    private List<MedicalModel> medicals;

    private Long licenseNumber;

    private String currentWork;



    //setters and getters


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Roles getRole() {
        return role;
    }

    public void serRole(Roles role) {
        this.role = role;
    }

    public List<MedicineModel> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<MedicineModel> medicines) {
        this.medicines = medicines;
    }

    public List<MedicalModel> getMedicals() {
        return medicals;
    }

    public void setMedicals(List<MedicalModel> medicals) {
        this.medicals = medicals;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public RequestVolunteer getRequest() {
        return request;
    }

    public void setRequest(RequestVolunteer request) {
        this.request = request;
    }

    public Long getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(Long licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getCurrentWork() {
        return currentWork;
    }

    public void setCurrentWork(String currentWork) {
        this.currentWork = currentWork;
    }

    //default constructor
    public MemberModel() {
    }

    //constructor for admin
    public MemberModel(String username, String email, String address){
        this.username =username;
        this.email = email;
        this.address = address;
    }

    //constructor
    public MemberModel(String username, String password, String email, Roles role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}
