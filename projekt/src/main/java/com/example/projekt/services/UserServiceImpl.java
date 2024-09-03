package com.example.projekt.services;

import com.example.projekt.models.*;
import com.example.projekt.repositories.RoleRepository;
import com.example.projekt.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByLogin(login);
        if(user.isEmpty()){
            new UsernameNotFoundException("User not exists by Username");
        }
        Set<GrantedAuthority> authorities = user.get().getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(login,user.get().getPassword(),authorities);
    }

    @Override
    public User getUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByLogin(login);
        if(user.isEmpty()){
            new UsernameNotFoundException("User not exists by Username");
        }
        return user.get();
    }

    public Optional<User> getUserByUsername2(String login) {
        return userRepo.findByLogin(login);
    }

    @Override
    public User save(UserRegisterDto userRegDto) {
        User user = new User();
        user.setLogin(userRegDto.getLogin());
        user.setPassword(passwordEncoder.encode(userRegDto.getPassword()));

        List<Role> roles=new ArrayList<Role>();
        Role role=new Role();
        role.setName("ROLE_USER");
        roles.add(role);
        user.setRoles(roles);



        return userRepo.save(user);
    }

    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Collection < Role > roles) {
        return roles.stream().map(role-> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}