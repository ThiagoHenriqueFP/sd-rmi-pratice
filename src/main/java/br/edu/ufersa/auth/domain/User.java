package br.edu.ufersa.auth.domain;

import java.io.Serializable;

public record User (
        String username,
        String password,
        UserAuthorities authorities
) implements Serializable{

}
