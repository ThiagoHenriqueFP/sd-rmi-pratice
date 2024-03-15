package br.edu.ufersa;

import br.edu.ufersa.server.GatewayApplication;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) {
        try {
//            System.setProperty("java.rmi.server.hostname","192.168.0.14");
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            GatewayApplication.INSTANCE();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}