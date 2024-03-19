package br.edu.ufersa.server.gateway;

import br.edu.ufersa.server.auth.domain.User;
import br.edu.ufersa.server.auth.exception.UserNotFoundException;
import br.edu.ufersa.server.gateway.domain.Car;
import br.edu.ufersa.server.gateway.domain.CarCategory;
import br.edu.ufersa.server.gateway.exception.ResourceNotFoundException;

import java.net.UnknownServiceException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GatewayCommonRemote extends Remote, GatewayEmployeeRemote {
    User login(String username, String password) throws RemoteException, UserNotFoundException;

    List<Car> getCarsByCategory(CarCategory category) throws RemoteException;

    List<Car> getAllCars() throws RemoteException;

    List<Car> searchCar(String model) throws RemoteException;

    Car searchCar(String model, String renavan) throws RemoteException, ResourceNotFoundException;

    //    void sendUpdates();
    Integer carsInStock() throws RemoteException;

    Car buy(String model, String renavan) throws RemoteException;
}
