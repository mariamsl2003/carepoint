package com.capstone.demo.Model;

import java.util.UUID;

import com.capstone.demo.Enum.RequestResult;
import com.capstone.demo.Enum.RequestToGet;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "medical")
public class MedicalModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private long quantity;

    private Long quantity_needed;

    private String item_image;

    private String date_image;

    private String description;

    private String prescript;

    // this is for accepting/rejecting to donate an item
    @Enumerated(EnumType.STRING)
    @Column(name = "request_result")
    private RequestResult requestResult;

    // this for accepting/rejecting the item to give it to a member
    @Enumerated(EnumType.STRING)
    private RequestToGet requested;

    // this is the member who requested it
    @ManyToOne
    @JoinColumn(name = "requester")
    private MemberModel requester;

    //the one who donate
    @ManyToOne
    @JoinColumn(name = "donor")
    private MemberModel donor;

   //getters and setters


    public Long getQuantity_needed() {
        return quantity_needed;
    }

    public void setQuantity_needed(Long quantity_needed) {
        this.quantity_needed = quantity_needed;
    }

    public String getPrescript() {
        return prescript;
    }

    public void setPrescript(String prescript) {
        this.prescript = prescript;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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


    public MedicalModel() {
    }

    public MedicalModel(String name, long quantity, MemberModel donor, RequestResult requestResult) {
        this.name = name;
        this.quantity = quantity;
        this.donor = donor;
        this.requestResult = requestResult;
    }
}
