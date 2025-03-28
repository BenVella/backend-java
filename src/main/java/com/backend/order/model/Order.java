package com.backend.order.model;

public class Order {
    private String id;
    private String customerDetails;
    private String installationAddress;
    private String preferredInstallationDate;
    private String timeSlotDetails;
    private String requiredProducts;
    private String requiredPackage;

    public String getRequiredPackage() {
        return requiredPackage;
    }

    public void setRequiredPackage(String requiredPackage) {
        this.requiredPackage = requiredPackage;
    }

    public String getRequiredProducts() {
        return requiredProducts;
    }

    public void setRequiredProducts(String requiredProducts) {
        this.requiredProducts = requiredProducts;
    }

    public String getTimeSlotDetails() {
        return timeSlotDetails;
    }

    public void setTimeSlotDetails(String timeSlotDetails) {
        this.timeSlotDetails = timeSlotDetails;
    }

    public String getPreferredInstallationDate() {
        return preferredInstallationDate;
    }

    public void setPreferredInstallationDate(String preferredInstallationDate) {
        this.preferredInstallationDate = preferredInstallationDate;
    }

    public String getInstallationAddress() {
        return installationAddress;
    }

    public void setInstallationAddress(String installationAddress) {
        this.installationAddress = installationAddress;
    }

    public String getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(String customerDetails) {
        this.customerDetails = customerDetails;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getters and Setters
}