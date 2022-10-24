package com.clean.architecture.form;

import com.clean.architecture.model.RoleModel;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthenticationResponse {
    private String token;
    private String userId;
    private Set<RoleModel> roles;
}
