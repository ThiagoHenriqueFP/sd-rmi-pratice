package br.edu.ufersa.server.gateway;

import br.edu.ufersa.server.auth.domain.User;
import br.edu.ufersa.server.gateway.domain.Car;
import br.edu.ufersa.server.gateway.domain.CarCategory;
import br.edu.ufersa.server.gateway.exception.PermissionNotAllowedException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GatewayEmployeeRemote extends Remote {
    boolean createCar(String renavam, String model, CarCategory category, Integer fabrication, Double price, User user) throws RemoteException, PermissionNotAllowedException;
    boolean deleteCar(String model, String renavan, User user) throws RemoteException, PermissionNotAllowedException;
    boolean deleteCar(String model, User user) throws RemoteException, PermissionNotAllowedException;
    Car update(Car car, User user) throws RemoteException, PermissionNotAllowedException;
}
