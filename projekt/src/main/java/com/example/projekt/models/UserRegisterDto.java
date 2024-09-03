package com.example.projekt.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDto {

    private String login;
    private String password;
    public UserRegisterDto(){
    }


}