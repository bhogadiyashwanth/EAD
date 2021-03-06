package com.wrath.client.dto;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.Objects;

public class User {
    private UserAddress address;
    private boolean isResident;
    private String profession;
    private boolean confirmed;
    private String _id;
    private String name;
    private String email;
    private boolean approved;
    private String token;
    private List<String> interests;

    public User() {
    }

    public User(UserAddress address, boolean isResident, String profession, boolean confirmed, String _id, String name, String email, boolean approved, String token, List<String> interests) {
        this.address = address;
        this.isResident = isResident;
        this.profession = profession;
        this.confirmed = confirmed;
        this._id = _id;
        this.name = name;
        this.email = email;
        this.approved = approved;
        this.token = token;
        this.interests = interests;
    }

    public boolean isApproved() {
        return approved;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean getApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress address) {
        this.address = address;
    }

    public boolean isResident() {
        return isResident;
    }

    public void setResident(boolean resident) {
        isResident = resident;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        return this._id.equals(((User) o)._id);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(address, isResident, profession, confirmed, _id, name, email, approved, token);
    }
}
