package com.udise.portal.vo.user;

import com.udise.portal.enums.Gender;
import com.udise.portal.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpReqVo {
    @NotBlank(message = "{Name is not blank}")
    @NotNull
    private String fullName;

    @NotBlank(message = "{Email is not blank}")
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",message="email invalid")
    @NotNull
    private String email;
    private String address;
    @NotBlank(message = "{mobile Number is not blank}")
    @NotNull
    @Pattern(regexp = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$",message = "invalid mobile number")
    private String mobile;

    private String userName;
    private String password;
    @NotBlank(message = "{field is not blank}")
    @NotNull
    private Gender gender;
    @NotBlank(message = "{field is not blank}")
    @NotNull
    private Role role;

    public @NotBlank(message = "{Name is not blank}") @NotNull String getFullName() {
        return fullName;
    }

    public void setFullName(@NotBlank(message = "{Name is not blank}") @NotNull String fullName) {
        this.fullName = fullName;
    }

    public @NotBlank(message = "{Email is not blank}") @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "email invalid") @NotNull String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "{Email is not blank}") @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "email invalid") @NotNull String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public @NotBlank(message = "{mobile Number is not blank}") @NotNull @Pattern(regexp = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$", message = "invalid mobile number") String getMobile() {
        return mobile;
    }

    public void setMobile(@NotBlank(message = "{mobile Number is not blank}") @NotNull @Pattern(regexp = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$", message = "invalid mobile number") String mobile) {
        this.mobile = mobile;
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

    public @NotBlank(message = "{field is not blank}") @NotNull Gender getGender() {
        return gender;
    }

    public void setGender(@NotBlank(message = "{field is not blank}") @NotNull Gender gender) {
        this.gender = gender;
    }

    public @NotBlank(message = "{field is not blank}") @NotNull Role getRole() {
        return role;
    }

    public void setRole(@NotBlank(message = "{field is not blank}") @NotNull Role role) {
        this.role = role;
    }
}
