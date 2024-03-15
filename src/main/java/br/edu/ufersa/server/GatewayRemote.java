package br.edu.ufersa.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GatewayRemote extends Remote {
    boolean login(String username, String password) throws RemoteException;
//    boolean createCar(String renvam, String name, CarCategory category, LocalDate fabrication, Double price);
//    boolean deleteCar(String name);
//    List<Car> getCarsByCategory(CarCategory category);
//    List<Car> getAllCars();
//    Car searchCar(String key);
//    Car update(Car car);
//    void sendUpdates();
//    void carsInStock();
//    void buy(Car car);
}
