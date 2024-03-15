package br.edu.ufersa.server;

import br.edu.ufersa.auth.AuthApplication;
import br.edu.ufersa.auth.AuthApplicationRemote;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Logger;

public class GatewayApplication implements GatewayRemote {
    private static final Logger logger = Logger.getLogger(GatewayApplication.class.getName());
    private final AuthApplicationRemote authStub;
    public static GatewayApplication INSTANCE;

    public static GatewayApplication INSTANCE() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new GatewayApplication();

                GatewayRemote skeleton = (GatewayRemote) UnicastRemoteObject.exportObject(INSTANCE, 0);
                Registry registry = LocateRegistry.getRegistry();
                registry.bind("gateway", skeleton);
                logger.info("gateway registered to registry");

                return INSTANCE;
            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            } catch (AlreadyBoundException e) {
                logger.severe("skeleton already registered");
            }
        }

        return INSTANCE;
    }

    public GatewayApplication() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();

        logger.info("starting auth server");
        AuthApplication.INSTANCE();
        this.authStub = (AuthApplicationRemote) registry.lookup("auth");
        logger.info("authStub created");
    }

    @Override
    public boolean login(String username, String password) throws RemoteException {
        logger.info("accessing auth application");
        return authStub.login(username, password);
    }
}
