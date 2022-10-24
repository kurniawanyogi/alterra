package com.clean.architecture.form;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;
}

