package com.practice.ecommercebackend.service;

import com.practice.ecommercebackend.api.model.LoginBody;
import com.practice.ecommercebackend.api.model.RegistrationBody;
import com.practice.ecommercebackend.exception.UserAlreadyExistsException;
import com.practice.ecommercebackend.model.LocalUser;
import com.practice.ecommercebackend.model.dao.LocalUserDAO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private EncryptionService encryptionService;
    private LocalUserDAO localUserDAO;
    private JWTService jwtService;
    public LocalUser registerUser( RegistrationBody registrationBody) throws UserAlreadyExistsException {

        if (localUserDAO.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent()||
        localUserDAO.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()){
            throw new UserAlreadyExistsException();
        }

        LocalUser user = new LocalUser();
        user.setEmail(registrationBody.getEmail());
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setUsername(registrationBody.getUsername());
        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));
        return user = localUserDAO.save(user);
    }

    public String loginUser(LoginBody loginBody){
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(loginBody.getUsername());
        if (opUser.isPresent()){
            LocalUser user = opUser.get();
            if (encryptionService.verifyPassword(loginBody.getPassword(),user.getPassword())){
                return jwtService.generateJWT(user);
            }

        }
        return null;
    }

}
