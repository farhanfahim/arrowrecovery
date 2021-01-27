package com.tekrevol.arrowrecovery.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tekrevol.arrowrecovery.managers.retrofit.GsonFactory;

public class UserDetails {

    @Expose
    @SerializedName("full_name")
    private String fullName;
    @Expose
    @SerializedName("image_url")
    private String imageUrl;
    @Expose
    @SerializedName("is_social_login")
    private int isSocialLogin;
    @Expose
    @SerializedName("email_updates")
    private int emailUpdates;
    @Expose
    @SerializedName("is_approved")
    private int isApproved;
    @Expose
    @SerializedName("is_verified")
    private int isVerified;

    @Expose
    @SerializedName("is_completed")
    private int isCompleted;
    @Expose
    @SerializedName("image")
    private String image;
    @Expose
    @SerializedName("zip_code")
    private String zipCode;
    @Expose
    @SerializedName("city")
    private String city;
    @Expose
    @SerializedName("address")
    private String address;
    @Expose
    @SerializedName("company")
    private String company;
    @Expose
    @SerializedName("phone")
    private String phone;
    @Expose
    @SerializedName("last_name")
    private String lastName;
    @Expose
    @SerializedName("first_name")
    private String firstName;
    @Expose
    @SerializedName("state")
    private String state;
    @Expose
    @SerializedName("kind_of_company")
    private String kindOfCompany;
    @Expose
    @SerializedName("country")
    private String country;

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    @Expose
    @SerializedName("user_type")
    private int userType;


    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("designation")
    private String designation;
    @Expose
    @SerializedName("category_id")
    private int categoryId;
    @Expose
    @SerializedName("user_id")
    private int userId;
    @Expose
    @SerializedName("parent_id")
    private int parentId;
    @Expose
    @SerializedName("about")
    private String about;
    @Expose
    @SerializedName("latitude")
    private double lat;
    @Expose
    @SerializedName("longitude")
    private double lng;
    @Expose
    @SerializedName("title")
    private int title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(int isApproved) {
        this.isApproved = isApproved;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getIsSocialLogin() {
        return isSocialLogin;
    }

    public void setIsSocialLogin(int isSocialLogin) {
        this.isSocialLogin = isSocialLogin;
    }

    public int getEmailUpdates() {
        return emailUpdates;
    }

    public void setEmailUpdates(int emailUpdates) {
        this.emailUpdates = emailUpdates;
    }

    public int getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(int isVerified) {
        this.isVerified = isVerified;
    }

    public int getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }


    public void setState(String state) {
        this.state = state;
    }

    public String getKindOfCompany() {
        return kindOfCompany;
    }

    public void setKindOfCompany(String kindOfCompany) {
        this.kindOfCompany = kindOfCompany;
    }


    @Override
    public String toString() {
        return GsonFactory.getSimpleGson().toJson(this);
    }
}
