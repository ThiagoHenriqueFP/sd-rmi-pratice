package br.edu.ufersa.server.gateway.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException() {
        super("car no found");
    }
}
