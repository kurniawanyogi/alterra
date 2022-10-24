package com.clean.architecture.controller;

import com.clean.architecture.common.ResponseUtil;
import com.clean.architecture.common.TokenUtil;
import com.clean.architecture.constant.MessageConstant;
import com.clean.architecture.form.AuthenticationResponse;
import com.clean.architecture.form.LoginRequest;
import com.clean.architecture.model.Authentication;
import com.clean.architecture.service.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/user")
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailServiceImpl userDetailsService;
    private final TokenUtil tokenUtil;

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception exception) {
            return ResponseUtil.build(MessageConstant.FAILED, exception.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        Authentication authentication = (Authentication) userDetailsService.loadUserByUsername(request.getUsername());
        String token = tokenUtil.generateToken(authentication);
        AuthenticationResponse response = new AuthenticationResponse(token, authentication.getUserId(), authentication.getUser().getRoles());
        return ResponseUtil.build(MessageConstant.SUCCESS, response, HttpStatus.OK);
    }

}