package br.edu.ufersa.auth;

import br.edu.ufersa.auth.domain.User;
import br.edu.ufersa.auth.domain.UserAuthorities;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.logging.Logger;

public class AuthApplication implements AuthApplicationRemote{
    private final static Logger logger = Logger.getLogger(AuthApplication.class.getName());
    public HashMap<String, User> userDatabase = new HashMap<>();
    private static AuthApplication INSTANCE;

    public AuthApplication() {
        userDatabase.put("admin", new User("admin", "admin01", UserAuthorities.EMPLOYEE));
        userDatabase.put("client", new User("client", "client", UserAuthorities.CLIENT));
    }

    public static AuthApplication INSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new AuthApplication();

            try {
                AuthApplicationRemote skeleton = (AuthApplicationRemote) UnicastRemoteObject.exportObject(INSTANCE(), 0);
                Registry registry = LocateRegistry.getRegistry();
                registry.bind("auth", skeleton);
                logger.info("auth server ready and registered");
            } catch (RemoteException | AlreadyBoundException e) {
                logger.severe(e.getClass().getName() + ": " + e.getMessage());
            }

            return INSTANCE;
        }

        return INSTANCE;
    }

    public boolean login(String username, String password) {
        logger.info("checking database");
        User user = userDatabase.get(username);

        if (user == null) {
            logger.info("user not found");
            return false;
        } else if (!user.password().equals(password)) {
            logger.info("credentials not matching");
            return false;
        }

        return true;
    }

    public boolean create(String username, String password, UserAuthorities authorities) {
        logger.info("checking database");
        User userAlreadyExists = userDatabase.get(username);

        if (userAlreadyExists != null) return false;

        User newUser = new User(username, password, authorities);

        logger.info("creating user");
        userDatabase.put(username, newUser);
        return true;
    }
}
