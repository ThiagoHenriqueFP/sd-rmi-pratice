package br.edu.ufersa.server.gateway;

import br.edu.ufersa.server.gateway.domain.Car;
import br.edu.ufersa.server.gateway.domain.CarCategory;
import br.edu.ufersa.server.gateway.exception.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class CarDatabase {
    private static final Logger logger = Logger.getLogger(CarDatabase.class.getName());
    private final HashMap<String, List<Car>> database = new HashMap<>();

    public CarDatabase() {
        logger.info("filling database");
        database.put("kwid", new ArrayList<>(){{
            add(new Car("kwid", CarCategory.ECONOMY, "0123", 2021, 48000.0));
            add(new Car("kwid", CarCategory.ECONOMY, "0121", 2023, 60000.0));
            add(new Car("kwid", CarCategory.ECONOMY, "0122", 2021, 53000.0));
            add(new Car("kwid", CarCategory.ECONOMY, "0124", 2022, 58000.0));
        }});

        database.put("civic", new ArrayList<>(){{
            add(new Car("civic", CarCategory.INTERMEDIARY, "0224", 2023, 210000.0));
            add(new Car("civic", CarCategory.INTERMEDIARY, "0223", 2024, 230000.0));
            add(new Car("civic", CarCategory.INTERMEDIARY, "0222", 2024, 270000.0));
            add(new Car("civic", CarCategory.INTERMEDIARY, "0221", 2001, 21000.0));
        }});

        database.put("sw4", new ArrayList<>(){{
            add(new Car("sw4", CarCategory.EXCLUSIVE, "0323", 2024, 500000.0));
            add(new Car("sw4", CarCategory.EXCLUSIVE, "0322", 2024, 500000.0));
            add(new Car("sw4", CarCategory.EXCLUSIVE, "0321", 2024, 500000.0));
            add(new Car("sw4", CarCategory.EXCLUSIVE, "0320", 2024, 500000.0));
        }});
    }

    private boolean renavanAlreadyExists(List<Car> list, String renavan) {
        return list.stream().anyMatch(it -> it.getRenavan().equals(renavan));
    }

    private Car getCarFromList(List<Car> list, String renavan) {
        return list.stream().filter(it -> it.getRenavan().equals(renavan)).findFirst().get();
    }

    public boolean save(Car car) {
        String key = car.getModel();

        if (!database.containsKey(key)) {
            List<Car> list = new ArrayList<>() {{
                add(car);
            }};

            database.put(key, list);
            return true;
        }

        List<Car> list = database.get(key);

        boolean renavanAlreadyExists = this.renavanAlreadyExists(list, car.getRenavan());

        if (renavanAlreadyExists) {
            logger.severe("renavan " + car.getRenavan() + " already exists");
            return false;
        }

        list.add(car);

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
            list.remove(this.getCarFromList(list, renavan));

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

    public Car search(String model, String renavan) {
        if (database.containsKey(model)) {
            var cars = database.get(model);

            var car = cars.stream().filter(it -> it.getRenavan().equals(renavan)).findFirst();
            if (car.isPresent())
                return car.get();
        }

        throw new ResourceNotFoundException();
    }

    public Car update(Car car) {
        if (database.containsKey(car.getModel())) {
            List<Car> cars = database.get(car.getModel());
            Car saved = this.getCarFromList(cars, car.getRenavan());
            cars.remove(saved);
            saved.update(car);
            this.save(saved);
            return saved;
        }

        return null;
    }

    public Car buy(String model, String renavan) {
        if (database.containsKey(model)) {
            var cars = database.get(model);

            var car = cars.stream().filter(it -> it.getRenavan().equals(renavan)).findFirst();
            if (car.isPresent()) {
                cars.remove(car.get());

                return car.get();
            }
        }

        throw new ResourceNotFoundException();
    }
}
