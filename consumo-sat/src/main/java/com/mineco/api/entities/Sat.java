package com.mineco.api.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sat {

    private String username;
    @Setter
    private String password;
    private Integer id;
    private String email;
    private Object roles;
    private String accessToken;
    private String tokenType;
}
