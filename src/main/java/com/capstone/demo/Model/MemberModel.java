package com.capstone.demo.Model;

import java.util.List;
import java.util.UUID;

import com.capstone.demo.Enum.Volunteering;

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
    @Column(unique = true)
    private String username;

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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Volunteering getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteering volunteer) {
        this.volunteer = volunteer;
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

    private String password;
    private long phoneNumber;
    private String imagePath;

    @Enumerated(EnumType.STRING)
    private Volunteering volunteer;

    @OneToMany(mappedBy = "member")
    private List<MedicineModel> medicines;

    @OneToMany(mappedBy = "member")
    private List<MedicalModel> medicals;

    public MemberModel(String username, String password, long phoneNumber, Volunteering volunteer) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.volunteer = volunteer;
    }
}
