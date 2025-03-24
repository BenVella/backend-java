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

  // Getters and setters

  public static class CustomerDetails {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @jakarta.validation.constraints.Email
    private String email;

    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    // Getters and setters
  }

  public static class InstallationDetails {

    @NotBlank
    private String address;

    @NotNull
    private LocalDateTime preferredDateTime;

    @NotBlank
    private String timeSlot;

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

    // Getters and setters
  }
}