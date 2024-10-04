package com.udise.portal.vo.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class ClientSignUpReqVo {
    @NotBlank(message = "{School Name is not blank}")
    @NotNull
    private String schoolName;
    @NotBlank(message = "{Name is not blank}")
    @NotNull
    private String contactPerson;
    @NotBlank(message = "{Email is not blank}")
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",message="email invalid")
    @NotNull
    private String email;
    @NotBlank(message = "{Contact Number is not blank}")
    @NotNull
    @Pattern(regexp = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$",message = "invalid mobile number")
    private String mobile;

   public @NotBlank(message = "{School Name is not blank}") @NotNull String getSchoolName() {
    return schoolName;
   }

   public void setSchoolName(@NotBlank(message = "{School Name is not blank}") @NotNull String schoolName) {
    this.schoolName = schoolName;
 }

 public @NotBlank(message = "{Name is not blank}") @NotNull String getContactPerson() {
  return contactPerson;
 }

 public void setContactPerson(@NotBlank(message = "{Name is not blank}") @NotNull String contactPerson) {
  this.contactPerson = contactPerson;
 }

 public @NotBlank(message = "{Email is not blank}") @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "email invalid") @NotNull String getEmail() {
  return email;
 }

 public void setEmail(@NotBlank(message = "{Email is not blank}") @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "email invalid") @NotNull String email) {
  this.email = email;
 }

 public @NotBlank(message = "{Contact Number is not blank}") @NotNull @Pattern(regexp = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$", message = "invalid mobile number") String getMobile() {
  return mobile;
 }

 public void setMobile(@NotBlank(message = "{Contact Number is not blank}") @NotNull @Pattern(regexp = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$", message = "invalid mobile number") String mobile) {
  this.mobile = mobile;
 }
}
