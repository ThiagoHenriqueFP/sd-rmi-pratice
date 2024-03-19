package br.edu.ufersa.server.gateway.exception;

public class PermissionNotAllowedException extends RuntimeException{
    public PermissionNotAllowedException() {
        super("this user doesn't has permission to call this method");
    }
}
