package com.capstone.demo.Model;

import java.lang.reflect.Member;
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

    private long quantity;

    private String item_image;

    private String date_image;

    // this for accepting/rejecting to have the item donated
    @Enumerated(EnumType.STRING)
    private RequestResult requestResult;

    // this for accepting/rejecting the item to give it to a member
    @Enumerated(EnumType.STRING)
    private RequestToGet requested;

    // this is the member who requested it
    @ManyToOne
    @JoinColumn(name = "member_to_get_id")
    private MemberModel requester;

    //member who donate
    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberModel donor;


    //getters ans setters

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

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getDate_image() {
        return date_image;
    }

    public void setDate_image(String date_image) {
        this.date_image = date_image;
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

    public MemberModel getRequester() {
        return requester;
    }

    public void setRequester(MemberModel requester) {
        this.requester = requester;
    }

    public MemberModel getDonor() {
        return donor;
    }

    public void setDonor(MemberModel donor) {
        this.donor = donor;
    }

    //constructor

    public MedicineModel(String name, long quantity,  MemberModel donor, RequestResult requestResult) {
        this.name = name;
        this.quantity = quantity;
        this.donor = donor;
        this.requestResult = requestResult;
    }
}
