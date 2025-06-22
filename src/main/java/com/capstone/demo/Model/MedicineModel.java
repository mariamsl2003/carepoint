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
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "medicine")
public class MedicineModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;;
    private String imagePath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public RequestResult getRequestResult() {
        return requestResult;
    }

    public void setRequestResult(RequestResult requestResult) {
        this.requestResult = requestResult;
    }

    public RequestToGet getRequested() {
        return requested;
    }

    public void setRequested(RequestToGet requested) {
        this.requested = requested;
    }

    public MemberModel getMemberToGet() {
        return memberToGet;
    }

    public void setMemberToGet(MemberModel memberToGet) {
        this.memberToGet = memberToGet;
    }

    public MemberModel getMember() {
        return member;
    }

    public void setMember(MemberModel member) {
        this.member = member;
    }

    // this for accepting/rejecting to have the item donated
    @Enumerated(EnumType.STRING)
    private RequestResult requestResult;

    // this for accepting/rejecting the item to give it to a member
    @Enumerated(EnumType.STRING)
    private RequestToGet requested;

    // this is the member who requested it
    @ManyToOne
    @JoinColumn(name = "member_to_get_id")
    private MemberModel memberToGet;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberModel member;

    public MedicineModel(String name, String description, MemberModel member) {
        this.name = name;
        this.description = description;
        this.member = member;
    }
}
