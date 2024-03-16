package br.edu.ufersa.server;

import br.edu.ufersa.auth.domain.User;
import br.edu.ufersa.server.domain.Car;
import br.edu.ufersa.server.domain.CarCategory;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Optional;

public interface GatewayRemote extends Remote {
    Optional<User> login(String username, String password) throws RemoteException;
    boolean createCar(String renavam, String model, CarCategory category, Integer fabrication, Double price) throws RemoteException;
    boolean deleteCar(String model, String renavan) throws RemoteException;
    boolean deleteCar(String model) throws RemoteException;
    List<Car> getCarsByCategory(CarCategory category) throws RemoteException;
    List<Car> getAllCars() throws RemoteException;
    List<Car> searchCar(String model);
    Optional<Car> searchCar(String model, String renavan);
    Car update(Car car);
//    void sendUpdates();
    Integer carsInStock();
    Optional<Car> buy(String model, String renavan);
}
