package com.udise.portal.user;

import com.udise.portal.vo.job.JobResVo;
import com.udise.portal.vo.user.UserResVo;

import java.util.List;

public interface UserManager {
    public List<UserResVo> getUsers(Long clientId);
}
