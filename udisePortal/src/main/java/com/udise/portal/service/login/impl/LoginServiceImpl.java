package com.udise.portal.service.login.impl;

import com.udise.portal.dao.AppUserDao;
import com.udise.portal.dao.ClientDao;
import com.udise.portal.dao.SuperAdminDao;
import com.udise.portal.entity.AppUser;
import com.udise.portal.entity.Client;
import com.udise.portal.entity.SuperAdmin;
import com.udise.portal.enums.Role;
import com.udise.portal.service.login.LoginManager;
import com.udise.portal.vo.client.ClientLoginReqVo;
import com.udise.portal.vo.client.ClientLoginResVo;

import com.udise.portal.vo.admin.SuperLoginResVo;
import com.udise.portal.vo.user.UserLoginReqVo;
import com.udise.portal.vo.user.UserLoginResVo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static com.udise.portal.common.JwtUtil.generateToken;

@Service
@Transactional
public class LoginServiceImpl implements LoginManager,UserDetailsService {
    @Autowired
    private AppUserDao appUserDao;

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private SuperAdminDao superAdminDao;

    @Override
    public ClientLoginResVo clientLogin(ClientLoginReqVo obj) {
        Client user = clientDao.findByEmail(obj.getUserName());
        if(user==null){
            throw new UsernameNotFoundException("User not found");
        }
        else if(!user.getPassword().equals(obj.getPassword())){
            throw new UsernameNotFoundException("Password Doesn't Match");
        }else{
            String token = generateToken(user.getEmail()); // Implement JWT token generation
            ClientLoginResVo response = new ClientLoginResVo();
            response.setId(user.getId());
            response.setAuthtoken(token);
            response.setMsg("Login SuccessFull");
            return response;
        }
    }

    @Override
    public UserLoginResVo userLogin(UserLoginReqVo obj) {
        AppUser user = appUserDao.findByEmail(obj.getUserName());
        Client client=clientDao.findByEmail(obj.getUserName());
        if(user==null){
            if(client==null){
                throw new UsernameNotFoundException("User not found");
            }else if(client!=null && !client.getPassword().equals(obj.getPassword())){
                throw new UsernameNotFoundException("Password Doesn't Match");
            }else{
                String token = generateToken(client.getEmail()); // Implement JWT token generation
                UserLoginResVo response = new UserLoginResVo();
                response.setId(client.getId());
                response.setAuthtoken(token);
                response.setMsg("Login SuccessFull");
                response.setRole(Role.CLIENT);
                response.setName(client.getContactPerson());
                return response;
            }

        }else if(user!=null && client!=null){
            if(!user.getPassword().equals(obj.getPassword()) && !client.getPassword().equals(obj.getPassword())){
                throw new UsernameNotFoundException("Password Doesn't Match");
            }
            else if(user.getPassword().equals(obj.getPassword())){
                String token = generateToken(user.getUserName()); // Implement JWT token generation
                UserLoginResVo response = new UserLoginResVo();
                response.setId(user.getId());
                response.setAuthtoken(token);
                response.setMsg("Login SuccessFull");
                response.setRole(Role.USER);
                response.setName(user.getFullName());
                return response;
            }else if(client.getPassword().equals(obj.getPassword())){
                String token = generateToken(client.getEmail()); // Implement JWT token generation
                UserLoginResVo response = new UserLoginResVo();
                response.setId(client.getId());
                response.setAuthtoken(token);
                response.setMsg("Login SuccessFull");
                response.setRole(Role.CLIENT);
                response.setName(client.getContactPerson());
                return response;
            }else{
                throw new UsernameNotFoundException("Password Doesn't Match");
            }
        }
        else if(!user.getPassword().equals(obj.getPassword())){
            throw new UsernameNotFoundException("Password Doesn't Match");
        }else{
                String token = generateToken(user.getUserName()); // Implement JWT token generation
                UserLoginResVo response = new UserLoginResVo();
                response.setId(user.getId());
                response.setAuthtoken(token);
                response.setMsg("Login SuccessFull");
                response.setRole(Role.USER);
                response.setName(user.getFullName());
                return response;
        }
    }

    @Override
    public ClientLoginResVo get(ClientLoginReqVo obj) {
        ClientLoginResVo response = new ClientLoginResVo();
        response.setAuthtoken("token");
        response.setMsg("Login SuccessFull");
        return response;
    }
    // used in jwtfilter for check token
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserDao.findByEmail(username);
        Client client=clientDao.findByEmail(username);
        SuperAdmin admin=superAdminDao.findByEmail(username);
        if (user == null && client==null && admin==null){
            throw new UsernameNotFoundException("User not found");
        }else if(user!=null){
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
        }else if(client!=null){
            return new org.springframework.security.core.userdetails.User(client.getEmail(), client.getPassword(), new ArrayList<>());
        }else{
            return new org.springframework.security.core.userdetails.User(admin.getUsername(), admin.getPassword(),  new ArrayList<>());
        }
    }

    @Override
    public SuperLoginResVo superLogin(UserLoginReqVo obj) {
        SuperAdmin user = superAdminDao.findByEmail(obj.getUserName());
        if (user != null) {
            if (user.getPassword().equals(obj.getPassword())) {
                String token = generateToken(user.getUsername()); // Implement JWT token generation
                SuperLoginResVo response = new SuperLoginResVo();
                response.setId(user.getId());
                response.setAuthtoken(token);
                response.setMsg("Login SuccessFull");
                response.setName(user.getFullName());
                response.setRole(Role.SUPER_ADMIN);
                return response;
            } else {
                throw new UsernameNotFoundException("Password Doesn't Match");
            }
        } else {
            throw new UsernameNotFoundException("Password Doesn't Match");
        }
    }
}
