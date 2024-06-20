package vn.hust.easypos.service.dto;

import java.io.Serializable;

public class StaffResponse implements Serializable {

    private Integer id;
    private String email;
    private String fullName;
    private String username; // tên đăng nhap
    private String phoneNumber;

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public StaffResponse(Integer id, String email, String fullName, String username, String phoneNumber, Integer status) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
