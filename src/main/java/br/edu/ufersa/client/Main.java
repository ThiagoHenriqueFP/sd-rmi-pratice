package br.edu.ufersa.client;

import br.edu.ufersa.server.GatewayRemote;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            GatewayRemote gatewayStub = (GatewayRemote) registry.lookup("gateway");

            boolean isLoged = gatewayStub.login("client", "client");
            System.out.println(isLoged);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
