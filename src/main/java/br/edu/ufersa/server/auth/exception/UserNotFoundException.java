package br.edu.ufersa.server.auth.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("user not found");
    }
}
