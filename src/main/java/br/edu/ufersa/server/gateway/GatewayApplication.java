package br.edu.ufersa.server.gateway;

import br.edu.ufersa.server.auth.AuthApplication;
import br.edu.ufersa.server.auth.AuthApplicationRemote;
import br.edu.ufersa.server.auth.domain.User;
import br.edu.ufersa.server.auth.exception.UserNotFoundException;
import br.edu.ufersa.server.gateway.domain.Car;
import br.edu.ufersa.server.gateway.domain.CarCategory;
import br.edu.ufersa.server.gateway.exception.PermissionNotAllowedException;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.logging.Logger;

public class GatewayApplication implements GatewayCommonRemote, GatewayEmployeeRemote {
    private static final Logger logger = Logger.getLogger(GatewayApplication.class.getName());
    private final CarDatabase database = new CarDatabase();
    private final AuthApplicationRemote authStub;
    public static GatewayApplication INSTANCE;

    public static GatewayApplication INSTANCE() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new GatewayApplication();

                GatewayCommonRemote skeleton = (GatewayCommonRemote) UnicastRemoteObject.exportObject(INSTANCE, 0);
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
    public User login(String username, String password) throws RemoteException, UserNotFoundException {
        logger.info("accessing auth application");
        return authStub.login(username, password);
    }

    @Override
    public List<Car> getCarsByCategory(CarCategory category) throws RemoteException {
        return database.getCarsByCategory(category);
    }

    @Override
    public List<Car> getAllCars() throws RemoteException {
        return database.getAllCars();
    }

    @Override
    public List<Car> searchCar(String model) {
        return database.search(model);
    }

    @Override
    public Car searchCar(String model, String renavan) {
        return database.search(model, renavan);
    }


    @Override
    public Integer carsInStock() {
        return database.getAllCars().size();
    }

    @Override
    public Car buy(String model, String renavan) {
        return database.buy(model, renavan);
    }

    @Override
    public Car update(Car car, User user) throws RemoteException {
        if(authStub.isEmployee(user)) return database.update(car);
        throw new PermissionNotAllowedException();
    }

    @Override
    public boolean createCar(String renavam, String name, CarCategory category, Integer fabrication, Double price, User user) throws RemoteException {
        Car car = new Car(name, category, renavam, fabrication, price);

        if(authStub.isEmployee(user)) return database.save(car);
        throw new PermissionNotAllowedException();
    }

    @Override
    public boolean deleteCar(String model, String renavan, User user) throws RemoteException {
        if(authStub.isEmployee(user)) return database.delete(model, renavan);
        throw new PermissionNotAllowedException();
    }

    @Override
    public boolean deleteCar(String model, User user) throws RemoteException {
        if(authStub.isEmployee(user)) return database.delete(model);
        throw new PermissionNotAllowedException();
    }
}
