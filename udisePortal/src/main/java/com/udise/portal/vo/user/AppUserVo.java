package com.udise.portal.vo.user;

import com.udise.portal.common.CoreUtil;
import com.udise.portal.entity.AppUser;
import com.udise.portal.enums.Gender;
import com.udise.portal.enums.RegistrationStatus;
import com.udise.portal.enums.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppUserVo {
    public static final String FULL_NAME = "fullName";

    public static final String ID = "id";

    private Long id;

    @NotBlank(message = "{appUser.fullName.notBlank}")
    @Size(max = 50, message = "{appuser.fullName.size}")
    private String fullName;

    @NotBlank(message = "{appUser.email.notBlank}")
    @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "{appUser.email.correct}")
    private String email;

    @NotBlank(message = "{appUser.mobile.notBlank}")
    @Size(min = 10, max = 10, message = "{appuser.mobile.size}")
    @Pattern(regexp = "[0-9]+", message = "{appUser.mobile.regex}")
    private String mobile;

    private boolean loginCreated;

    private Gender gender;

    @Valid
    private Role role;

    private Long clientId;

    private RegistrationStatus registrationStatus;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public AppUserVo(final AppUser appuser) {
        prepare(appuser);
    }

    public AppUserVo(final Long id) {
        this.id = id;
    }

    private void prepare(AppUser existing) {
        setId(existing.getId());
        setFullName(existing.getFullName());
        if (CoreUtil.isNotNull(existing.getGender())) {
            setGender(existing.getGender());
        }
        setEmail(existing.getEmail());
        setMobile(existing.getMobile());
    }

    public AppUserVo() {
        super();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "{appUser.fullName.notBlank}") @Size(max = 50, message = "{appuser.fullName.size}") String getFullName() {
        return fullName;
    }

    public void setFullName(@NotBlank(message = "{appUser.fullName.notBlank}") @Size(max = 50, message = "{appuser.fullName.size}") String fullName) {
        this.fullName = fullName;
    }

    public @NotBlank(message = "{appUser.email.notBlank}") @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "{appUser.email.correct}") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "{appUser.email.notBlank}") @Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "{appUser.email.correct}") String email) {
        this.email = email;
    }

    public @NotBlank(message = "{appUser.mobile.notBlank}") @Size(min = 10, max = 10, message = "{appuser.mobile.size}") @Pattern(regexp = "[0-9]+", message = "{appUser.mobile.regex}") String getMobile() {
        return mobile;
    }

    public void setMobile(@NotBlank(message = "{appUser.mobile.notBlank}") @Size(min = 10, max = 10, message = "{appuser.mobile.size}") @Pattern(regexp = "[0-9]+", message = "{appUser.mobile.regex}") String mobile) {
        this.mobile = mobile;
    }

    public boolean isLoginCreated() {
        return loginCreated;
    }

    public void setLoginCreated(boolean loginCreated) {
        this.loginCreated = loginCreated;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public @Valid Role getRole() {
        return role;
    }

    public void setRole(@Valid Role role) {
        this.role = role;
    }

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public AppUserVo toconvertEntity(AppUser appUser) {
        setFullName(appUser.getFullName());

        return this;
    }

    public AppUser merge(AppUser existAppUser) {
        existAppUser.setEmail(getEmail());
        existAppUser.setFullName(getFullName());
        existAppUser.setMobile(getMobile());
        if (null != getGender()) {
            existAppUser.setGender(Gender.valueOf(getGender().getDisplayName()));
        }

        return existAppUser;
    }


}
