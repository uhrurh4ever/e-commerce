package com.practice.ecommercebackend.api.controller.auth;

import com.practice.ecommercebackend.api.model.LoginBody;
import com.practice.ecommercebackend.api.model.LoginResponse;
import com.practice.ecommercebackend.api.model.RegistrationBody;
import com.practice.ecommercebackend.exception.UserAlreadyExistsException;
import com.practice.ecommercebackend.model.LocalUser;
import com.practice.ecommercebackend.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
@Getter
@Setter
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private UserService userService;


    @SuppressWarnings("rawtypes")
    @PostMapping("/register")
    public ResponseEntity registerUser( @Valid @RequestBody RegistrationBody registrationBody) {
        try {
            userService.registerUser(registrationBody);
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity <LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody){
        String jwt = userService.loginUser(loginBody);
        if (jwt==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        LoginResponse response = new LoginResponse();
        response.setJwt(jwt);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user){
        return user;
    }
}