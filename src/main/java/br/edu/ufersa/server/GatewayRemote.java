package br.edu.ufersa.server;

import br.edu.ufersa.server.domain.Car;
import br.edu.ufersa.server.domain.CarCategory;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface GatewayRemote extends Remote {
    boolean login(String username, String password) throws RemoteException;
    boolean createCar(String renavam, String model, CarCategory category, Integer fabrication, Double price) throws RemoteException;
    boolean deleteCar(String model, String renavan) throws RemoteException;
    boolean deleteCar(String model) throws RemoteException;
    List<Car> getCarsByCategory(CarCategory category) throws RemoteException;
    List<Car> getAllCars() throws RemoteException;
//    Car searchCar(String key);
//    Car update(Car car);
//    void sendUpdates();
//    void carsInStock();
//    void buy(Car car);
}
