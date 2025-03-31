package com.capstone.demo.Model;

import java.util.List;
import java.util.UUID;

import com.capstone.demo.Enum.Volunteering;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "member")
public class MemberModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String username;
    private String password;
    private long phoneNumber;

    @Enumerated(EnumType.STRING)
    private Volunteering volunteer;

    @OneToMany(mappedBy = "member")
    private List<MedicineModel> medicines;

    // medical list
    // image profile after welaya replied

    public MemberModel(String username, String password, long phoneNumber, Volunteering volunteer) {
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.volunteer = volunteer;
    }
}
