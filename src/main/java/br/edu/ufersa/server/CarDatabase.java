package br.edu.ufersa.server;

import br.edu.ufersa.server.domain.Car;
import br.edu.ufersa.server.domain.CarCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class CarDatabase {
    private static final Logger logger = Logger.getLogger(CarDatabase.class.getName());
    private final HashMap<String, List<Car>> database = new HashMap<>();

    private boolean renavanAlreadyExists(List<Car> list, String renavan) {
        return list.stream().anyMatch(it -> it.getRenavan().equals(renavan));
    }

    private Car getCarFromlist(List<Car> list, String renavan) {
        return list.stream().filter(it -> it.getRenavan().equals(renavan)).findFirst().get();
    }

    public boolean save(Car car) {
        String key = car.getModel();

        if (!database.containsKey(key)) {
            List<Car> list = new ArrayList<>() {{
                add(car);
            }};

            database.put(key, list);
        }

        List<Car> list = database.get(key);

        boolean renavanAlreadyExists = this.renavanAlreadyExists(list, car.getRenavan());

        if (renavanAlreadyExists) {
            logger.severe("renavan " + car.getRenavan() + " already exists");
            return false;
        }

        logger.info("car successfully saved in database");
        return true;
    }

    public boolean delete(String model, String renavan) {
        if (!database.containsKey(model)) {
            logger.info("this model doesn't exists in database");
            return false;
        }

        List<Car> list = database.get(model);

        if (this.renavanAlreadyExists(list, renavan)) {
            list.remove(this.getCarFromlist(list, renavan));

            if (list.isEmpty()) {
                logger.info("removing model from database");
                database.remove(model);
            }

            return true;
        }

        return false;
    }

    public boolean delete(String model) {
        logger.info("removing model from database");
        database.remove(model);
        return true;
    }

    public List<Car> getCarsByCategory(CarCategory category) {
        List<Car> list = new ArrayList<>();

        for (String key : database.keySet()) {
            List<Car> cars = database.get(key);
            CarCategory c= cars.get(0).getCategory();

            if (c == category)
                list.addAll(cars);
        }

        return list;
    }

    public List<Car> getAllCars() {
        List<Car> list = new ArrayList<>();

        for (String key : database.keySet()) {
            List<Car> cars = database.get(key);
            list.addAll(cars);
        }

        return list;
    }

    public List<Car> search(String model) {
        if (database.containsKey(model)) {
            return database.get(model);
        }

        return new ArrayList<>();
    }

    public Optional<Car> search(String model, String renavan) {
        if (database.containsKey(model)) {
            var cars = database.get(model);

            return cars.stream().filter(it -> it.getRenavan().equals(renavan)).findFirst();
        }

        return Optional.empty();
    }

    public Car update(Car car) {
        if (database.containsKey(car.getModel())) {
            List<Car> cars = database.get(car.getModel());
            Car saved = this.getCarFromlist(cars, car.getRenavan());
            cars.remove(saved);
            saved.update(car);
            this.save(saved);
            return saved;
        }

        return null;
    }

    public Optional<Car> buy(String model, String renavan) {
        if (database.containsKey(model)) {
            var cars = database.get(model);

            var car = cars.stream().filter(it -> it.getRenavan().equals(renavan)).findFirst();
            cars.remove(car.get());

            return car;
        }

        return Optional.empty();
    }
}
