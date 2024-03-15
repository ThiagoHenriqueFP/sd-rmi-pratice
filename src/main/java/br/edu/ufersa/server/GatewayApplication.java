package br.edu.ufersa.server;

import br.edu.ufersa.auth.AuthApplication;
import br.edu.ufersa.auth.AuthApplicationRemote;
import br.edu.ufersa.server.domain.Car;
import br.edu.ufersa.server.domain.CarCategory;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class GatewayApplication implements GatewayRemote {
    private static final Logger logger = Logger.getLogger(GatewayApplication.class.getName());
    private final CarDatabase database = new CarDatabase();
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

    @Override
    public boolean createCar(String renavam, String name, CarCategory category, Integer fabrication, Double price) {
        Car car = new Car(name, category, renavam, fabrication, price);

        return database.save(car);
    }

    @Override
    public boolean deleteCar(String model, String renavan) throws RemoteException {
        return database.delete(model, renavan);
    }
    @Override
    public boolean deleteCar(String model) throws RemoteException {
        return database.delete(model);
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
    public Optional<Car> searchCar(String model, String renavan) {
        return database.search(model, renavan);
    }

    @Override
    public Car update(Car car) {
        return database.update(car);
    }

    @Override
    public Integer carsInStock() {
        return database.getAllCars().size();
    }

    @Override
    public Optional<Car> buy(String model, String renavan) {
        return database.buy(model, renavan);
    }
}
