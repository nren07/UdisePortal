package com.udise.portal.service.sign_up.impl;

import com.udise.portal.common.JwtUtil;
import com.udise.portal.dao.AbstractDao;
import com.udise.portal.dao.AppUserDao;
import com.udise.portal.dao.ClientDao;
import com.udise.portal.enums.RegistrationStatus;
import com.udise.portal.enums.Role;
import com.udise.portal.entity.AppUser;
import com.udise.portal.entity.Client;
import com.udise.portal.service.common_impl.PasswordGenerator;

import com.udise.portal.service.sign_up.SignUpManager;
import com.udise.portal.vo.client.ClientSignUpReqVo;
import com.udise.portal.vo.client.ClientSignUpResVo;
import com.udise.portal.vo.user.UserSignUpReqVo;
import com.udise.portal.vo.user.UserSignUpResVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
public class SignUpServiceImp implements SignUpManager{

	@Autowired
    private AbstractDao abstractDao;
    @Autowired
    private AppUserDao appUserDao;

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

	@Override
    public ClientSignUpResVo registerClient(ClientSignUpReqVo obj) {
        Client alreadyReg= clientDao.findByEmail(obj.getEmail());
        if(alreadyReg!=null){
            ClientSignUpResVo res=new ClientSignUpResVo();
            res.setMsg("Client Already Registered");
            return res;
        }
        Client client =new Client();
        Assert.notNull(obj, "filed cannot be null.");
        BeanUtils.copyProperties(obj, client);
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useLower(true)
                .useUpper(true)
                .useDigits(true)
                .usePunctuation(false) // or true, depending on your requirements
                .build();
        client.setPassword(PasswordGenerator.generate(8));
        client.setRole(Role.Client);
        BeanUtils.copyProperties(obj, client);
        Client save = abstractDao.save(client);
        client.setId(save.getId());
        return new ClientSignUpResVo(save.getId(),save.getEmail(),save.getPassword(),"Client Registered Successfully");
    }

    @Override
    public UserSignUpResVo registerUser(Long id,UserSignUpReqVo obj) throws Exception {
        Client client = clientDao.getById(Client.class,id);
        if(client==null){
            return new UserSignUpResVo(obj.getEmail(), null,"Client Not Registered");
        }
        AppUser alreadyReg=appUserDao.getUserByUserName(obj.getEmail());
        if(alreadyReg!=null){
            return new UserSignUpResVo(obj.getEmail(), null,"User Already Registered");
        }
        AppUser user=new AppUser();
        Assert.notNull(obj, "filed cannot be null.");
        BeanUtils.copyProperties(obj, user);
        System.out.println("school id is "+id);

        System.out.println(client.getSchoolName());
        user.setSchool(client);
        user.setUserName(obj.getEmail());
        PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                .useLower(true)
                .useUpper(true)
                .useDigits(true)
                .usePunctuation(false) // or true, depending on your requirements
                .build();
        user.setPassword(PasswordGenerator.generate(8));
        user.setRole(Role.User);
        user.setRegistrationStatus(RegistrationStatus.REQUESTED);
        AppUser save=appUserDao.save(user);
        return new UserSignUpResVo(save.getUserName(),save.getPassword(),"User Registered Successfully");
    }

}
