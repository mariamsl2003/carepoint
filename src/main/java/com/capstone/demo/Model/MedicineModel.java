package com.capstone.demo.Model;

import java.util.UUID;

import com.capstone.demo.Enum.RequestResult;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "medicine")
public class MedicineModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    private String imagePath;

    @Enumerated(EnumType.STRING)
    private RequestResult requestResult;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberModel member;

    public MedicineModel(String name, String description, MemberModel member) {
        this.name = name;
        this.description = description;
        this.member = member;
    }
}
