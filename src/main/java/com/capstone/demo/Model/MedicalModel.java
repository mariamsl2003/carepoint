package com.capstone.demo.Model;

import java.util.UUID;

import com.capstone.demo.Enum.RequestResult;
import com.capstone.demo.Enum.RequestToGet;

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
@Table(name = "medical")
public class MedicalModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    private String imagePath;

    // this is for accepting/rejecting to donate an item
    @Enumerated(EnumType.STRING)
    private RequestResult requestResult;

    // this for accepting/rejecting the item to give it to a member
    @Enumerated(EnumType.STRING)
    private RequestToGet requested;

    // this is the member who requested it
    private MemberModel memberToGet;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberModel member;

    public MedicalModel(String name, String description, MemberModel member) {
        this.name = name;
        this.description = description;
        this.member = member;
    }
}
