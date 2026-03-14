package com.homebase.ecom.checkout.api.dto;

import jakarta.validation.constraints.*;

/**
 * Address DTO
 */
public class AddressDTO {

    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 50)
    private String lastName;

    @NotBlank(message = "Address line 1 is required")
    @Size(min = 1, max = 100)
    private String addressLine1;

    @Size(max = 100)
    private String addressLine2;

    @NotBlank(message = "City is required")
    @Size(min = 1, max = 50)
    private String city;

    @NotBlank(message = "State is required")
    @Size(min = 2, max = 50)
    private String state;

    @NotBlank(message = "Zip code is required")
    @Pattern(regexp = "^[0-9]{5}(-[0-9]{4})?$", message = "Invalid zip code format")
    private String zipCode;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 2)
    private String country;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phone;

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
