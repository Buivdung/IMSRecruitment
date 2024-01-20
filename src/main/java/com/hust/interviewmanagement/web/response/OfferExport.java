package com.hust.interviewmanagement.web.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class OfferExport {
    @SerializedName("Candidate name")
    private String candidateName;
    @SerializedName("Email")
    private String email;
    @SerializedName("Interview notes")
    private String interviewNotes;
    @SerializedName("Department")
    private String department;
    @SerializedName("Status")
    private String status;
    @SerializedName("Contract type")
    private String contractType;
    @SerializedName("Create date")
    private String createDate;
    @SerializedName("Due date")
    private String dueDate;
    @SerializedName("Basic salary")
    private String basicSalary;
    @SerializedName("Notes")
    private String notes;
    @SerializedName("Approved")
    private String approved;
}
