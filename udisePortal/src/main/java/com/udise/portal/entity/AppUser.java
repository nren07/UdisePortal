package com.udise.portal.entity;


import com.udise.portal.enums.Gender;
import com.udise.portal.enums.RegistrationStatus;
import com.udise.portal.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Table(name = "app_user")
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {
    public static final String NAME = "fullName";
    public static final String MOBILE = "mobile";
    public static final String EMAIL = "email";
    public static final String USERNAME = "userName";
    public static final String PASSWORD = "password";
    public static final String ROLE = "role";
    public static final String REGISTRATION_STATUS = "registrationStatus";
    public static final String PENDING_JOBS="pendingJobs";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fullName;
    private String email;
    private String userName;
    private String password;
    private String mobile;
    private Gender gender;
    private Role role;
    private RegistrationStatus registrationStatus;
    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Client.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(
            name = "full_name",
            length = 255,
            nullable = true
    )
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    @Column(
            name = "email",
            length = 100,
            nullable = true
    )
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Enumerated(EnumType.STRING)
    @Column(
            name = "gender",
            nullable = true
    )
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }
    @Enumerated(EnumType.STRING)
    @Column(
            name = "role",
            nullable = true
    )
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Client getSchool() {
        return client;
    }

    public void setSchool(Client client) {
        this.client = client;
    }
}

