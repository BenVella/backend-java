package com.backend.order.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class CreateOrderRequest {


    @NotNull
    private CustomerDetails customerDetails;

    @NotNull
    private InstallationDetails installationDetails;

    @NotEmpty
    private List<ProductDetails> products;

    public List<ProductDetails> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDetails> products) {
        this.products = products;
    }

    public InstallationDetails getInstallationDetails() {
        return installationDetails;
    }

    public void setInstallationDetails(InstallationDetails installationDetails) {
        this.installationDetails = installationDetails;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }


    public static class CustomerDetails {
        @NotBlank
        private String name;

        @NotBlank
        @Pattern(regexp = "\\d{8}", message = "Phone number must be 10 digits")
        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class InstallationDetails {

        @NotBlank
        private String address;

        @NotNull
        private LocalDateTime preferredDateTime;

        @NotBlank
        private String timeSlot;

        public String getTimeSlot() {
            return timeSlot;
        }

        public void setTimeSlot(String timeSlot) {
            this.timeSlot = timeSlot;
        }

        public LocalDateTime getPreferredDateTime() {
            return preferredDateTime;
        }

        public void setPreferredDateTime(LocalDateTime preferredDateTime) {
            this.preferredDateTime = preferredDateTime;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        // Getters and setters
    }

    public enum ProductType {
        INTERNET,
        TV,
        TELEPHONY,
        MOBILE
    }

    public enum PackageType {
        INTERNET_250_MBPS,
        INTERNET_1_GBPS,
        TV_90_CHANNELS,
        TV_140_CHANNELS,
        FREE_ON_NET_CALLS,
        UNLIMITED_CALLS,
        MOBILE_PREPAID,
        MOBILE_POSTPAID
    }

    public static class ProductDetails {

        @NotNull
        private ProductType productType;

        @NotNull
        private PackageType packageType;

        public ProductDetails(ProductType productType, PackageType packageType) {
            this.productType = productType;
            this.packageType = packageType;
        }

        public PackageType getPackageType() {
            return packageType;
        }

        public void setPackageType(PackageType packageType) {
            this.packageType = packageType;
        }

        public ProductType getProductType() {
            return productType;
        }

        public void setProductType(ProductType productType) {
            this.productType = productType;
        }

        // Getters and setters
    }
}