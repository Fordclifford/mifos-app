package org.lspl.mobile.models;

public class GuarantorLoanModel {
    private String clientId;
    private String entityId;
    private String firstname;

    /* renamed from: id */
    private String f787id;
    private String id_number;
    private String lastname;
    private String loanId;
    private String mobile_number;

    public GuarantorLoanModel(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
        this.mobile_number = str;
        this.id_number = str2;
        this.firstname = str3;
        this.lastname = str4;
        this.clientId = str5;
        this.loanId = str6;
        this.f787id = str7;
        this.entityId = str8;
    }

    public String getMobile_number() {
        return this.mobile_number;
    }

    public String getId_number() {
        return this.id_number;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getLoanId() {
        return this.loanId;
    }

    public String getEntityId() {
        return this.entityId;
    }

    public String getId() {
        return this.f787id;
    }
}
