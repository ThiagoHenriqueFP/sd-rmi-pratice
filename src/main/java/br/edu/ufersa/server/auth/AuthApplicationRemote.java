package br.edu.ufersa.server.auth;

import br.edu.ufersa.server.auth.domain.User;
import br.edu.ufersa.server.auth.domain.UserAuthorities;
import br.edu.ufersa.server.auth.exception.UserNotFoundException;

import java.net.UnknownServiceException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Optional;

public interface AuthApplicationRemote extends Remote {
    User login (String username, String password) throws RemoteException, UserNotFoundException;
    boolean create (String username, String password, UserAuthorities authorities) throws RemoteException;
    boolean isEmployee(User user) throws RemoteException;
}
