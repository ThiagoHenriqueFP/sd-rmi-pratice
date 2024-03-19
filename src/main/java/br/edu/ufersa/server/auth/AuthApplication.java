package br.edu.ufersa.server.auth;

import br.edu.ufersa.server.auth.domain.User;
import br.edu.ufersa.server.auth.domain.UserAuthorities;
import br.edu.ufersa.server.auth.exception.UserNotFoundException;

import java.net.UnknownServiceException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Optional;
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

    public User login(String username, String password) throws UserNotFoundException {
        logger.info("checking database");
        User user = userDatabase.get(username);

        if (user == null || !user.password().equals(password)) {
            logger.info("user not found or credentials not matching");
            throw new UserNotFoundException();
        }

        logger.info("user successfully logged");
        return user;
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

    @Override
    public boolean isEmployee(User user) {
        logger.info("checking permissions");
        return user.authorities().equals(UserAuthorities.EMPLOYEE);
    }
}
