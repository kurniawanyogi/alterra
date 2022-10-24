package com.clean.architecture.service;

import com.clean.architecture.model.Authentication;
import com.clean.architecture.model.RoleModel;
import com.clean.architecture.model.UserModel;
import com.clean.architecture.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserModel> optionalUser = userRepository.findByUsername(username);

        if (!optionalUser.isPresent()) return null;
        if (optionalUser.get().isDeleted())
            return null;

        return new Authentication(optionalUser.get().getUsername(), optionalUser.get().getPassword(), true, true, true, true,
                getAuthorities(optionalUser.get()), optionalUser.get());
    }

    public Collection<GrantedAuthority> getAuthorities(UserModel user) {
        Set<RoleModel> roles = user.getRoles();
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        for (RoleModel role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        }
        return authorities;
    }
}
