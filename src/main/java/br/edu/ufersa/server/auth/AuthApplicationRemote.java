package br.edu.ufersa.server.auth;

import br.edu.ufersa.server.auth.domain.User;
import br.edu.ufersa.server.auth.domain.UserAuthorities;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Optional;

public interface AuthApplicationRemote extends Remote {
    Optional<User> login (String username, String password) throws RemoteException;
    boolean create (String username, String password, UserAuthorities authorities) throws RemoteException;
}
