package br.edu.ufersa.auth;

import br.edu.ufersa.auth.domain.UserAuthorities;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthApplicationRemote extends Remote {
    boolean login (String username, String password) throws RemoteException;
    boolean create (String username, String password, UserAuthorities authorities) throws RemoteException;
}
