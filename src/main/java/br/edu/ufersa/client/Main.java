package br.edu.ufersa.client;

import br.edu.ufersa.server.auth.domain.User;
import br.edu.ufersa.server.auth.domain.UserAuthorities;
import br.edu.ufersa.server.auth.exception.UserNotFoundException;
import br.edu.ufersa.server.gateway.GatewayCommonRemote;
import br.edu.ufersa.server.gateway.domain.Car;
import br.edu.ufersa.server.gateway.domain.CarCategory;
import br.edu.ufersa.server.gateway.exception.PermissionNotAllowedException;
import br.edu.ufersa.server.gateway.exception.ResourceNotFoundException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Main.class.getName());
        GatewayCommonRemote gatewayStub;
        try {
            Registry registry = LocateRegistry.getRegistry();
            gatewayStub = (GatewayCommonRemote) registry.lookup("gateway");
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }

        Scanner sc = new Scanner(System.in);

        User userSession = null;
        boolean isLogged = false;
        while (!isLogged) {
            System.out.println("informe login e senha");
            System.out.println("login: ");
            String username = sc.nextLine();
            System.out.println("senha: ");
            String password = sc.nextLine();

            User user;
            try {
                user = gatewayStub.login(username, password);
                if (user != null) {
                    userSession = user;
                    System.out.println("login realizado");
                    isLogged = true;
                } else {
                    System.out.println("login incorreto, tente novamente");
                }
            } catch (RemoteException | UserNotFoundException e) {
                System.out.println(e.getClass());
            }
        }

        String option = "";
        while (!option.equals("exit")) {
            if (userSession.authorities().equals(UserAuthorities.EMPLOYEE)) {
                System.out.println("""
                    | Option |          Service           |
                    |--------|----------------------------|
                    |      1 | create car                 |
                    |      2 | delete one car             |
                    |      3 | delete all cars from model |
                    |      4 | get cars by category       |
                    |      5 | get all cars               |
                    |      6 | search cars by model       |
                    |      7 | search one car             |
                    |      8 | update                     |
                    |      9 | number of cars in stock    |
                    |     10 | buy a car                  |
                    enter "exit" to shutdown
                    """);

                option = sc.nextLine();

                try {
                    switch (option) {
                        case "exit" -> {
                        }
                        case "1" -> {
                            String model, renavan;
                            CarCategory category;
                            int year;
                            double price;
                            System.out.println("informe as caracteristicas do carro\nmodelo: ");
                            model = sc.nextLine();
                            System.out.println("renavan: ");
                            renavan = sc.nextLine();
                            System.out.println("ano: ");
                            year = sc.nextInt();
                            System.out.println("categoria: [0: ECONOMY, 1: INTERMEDIARY, 2: EXCLUSIVE]");
                            int categoryInteger = sc.nextInt();
                            category = CarCategory.fromNumber(categoryInteger);
                            System.out.println("valor: ");
                            price = sc.nextDouble();

                            boolean isSuccess = gatewayStub.createCar(renavan, model, category, year, price, userSession);

                            if (isSuccess) System.out.println("carro cadastrado com sucesso");
                            else System.out.println("carro nao cadastrado, tente novamente");
                        }
                        case "2" -> {
                            String model, renavan;

                            System.out.println("informe o modelo");
                            model = sc.nextLine();
                            System.out.println("informe o renvan");
                            renavan = sc.nextLine();

                            if (gatewayStub.deleteCar(model, renavan, userSession))
                                System.out.println("carro deletado com sucesso");
                            else System.out.println("erro ao deletar carro");
                        }
                        case "3" -> {
                            String model;

                            System.out.println("informe o modelo");
                            model = sc.nextLine();
                            if (gatewayStub.deleteCar(model, userSession))
                                System.out.println("modelos removidos com sucesso");
                            else System.out.println("erro ao deletar modelos");
                        }
                        case "4" -> {
                            int index;

                            System.out.println("informe a categoria [0: ECONOMY, 1: INTERMEDIARY, 2: EXCLUSIVE]");
                            index = sc.nextInt();

                            System.out.println(gatewayStub.getCarsByCategory(CarCategory.fromNumber(index)));
                        }
                        case "5" -> {
                            var cars = gatewayStub.getAllCars();
                            if (cars.isEmpty()) System.out.println("nao ha carros cadastrados");
                            else System.out.println(cars);
                        }
                        case "6" -> {
                            String model;

                            System.out.println("informe o modelo");
                            model = sc.nextLine();
                            var cars = gatewayStub.searchCar(model);
                            if (cars.isEmpty()) System.out.println("nao ha carros para esse modelo");
                            else System.out.println(cars);
                        }
                        case "7" -> {
                            String model, renavan;

                            System.out.println("informe o modelo");
                            model = sc.nextLine();
                            System.out.println("informe o renvan");
                            renavan = sc.nextLine();

                            var car = gatewayStub.searchCar(model, renavan);

                            if (car != null) System.out.println(car);
                            else System.out.println("carro nao encontrado");
                        }
                        case "8" -> {
                            String model, renavan;
                            CarCategory category;
                            int year;
                            double price;
                            System.out.println("informe as caracteristicas do carro\nmodelo: ");
                            model = sc.nextLine();
                            System.out.println("renavan: ");
                            renavan = sc.nextLine();

                            Car carAlreadyExists = gatewayStub.searchCar(model, renavan);

                            if (carAlreadyExists != null) {
                                System.out.println("ano: ");
                                year = sc.nextInt();
                                System.out.println("categoria: [0: ECONOMY, 1: INTERMEDIARY, 2: EXCLUSIVE]");
                                int categoryInteger = sc.nextInt();
                                category = CarCategory.fromNumber(categoryInteger);
                                System.out.println("valor: ");
                                price = sc.nextDouble();

                                carAlreadyExists.setYear(year);
                                carAlreadyExists.setCategory(category);
                                carAlreadyExists.setPrice(price);

                                gatewayStub.update(carAlreadyExists, userSession);

                                System.out.println("carro atualizado\n" + carAlreadyExists);
                            } else System.out.println("carro nao encontrado na base de dados");
                        }
                        case "9" -> System.out.println("qtd carros em estoque: " + gatewayStub.carsInStock());
                        case "10" -> {
                            String model, renavan;

                            System.out.println("informe o modelo");
                            model = sc.nextLine();
                            System.out.println("informe o renvan");
                            renavan = sc.nextLine();
                            var car = gatewayStub.buy(model, renavan);

                            if (car != null) System.out.println(car);
                            else System.out.println("o carro nao pode ser comprado");
                        }
                    }
                } catch (RemoteException | PermissionNotAllowedException e) {
                    logger.severe(e.getClass().getName() + ": " + e.getMessage());
                }
            }  else {
                System.out.println("""
                    | Option |          Service           |
                    |--------|----------------------------|
                    |      1 | get cars by category       |
                    |      2 | get all cars               |
                    |      3 | search cars by model       |
                    |      4 | search one car             |
                    |      5 | number of cars in stock    |
                    |      6 | buy a car                  |
                    enter "exit" to shutdown
                    """);

                option = sc.nextLine();
                try {
                    switch (option) {
                        case "exit" -> {
                        }
                        case "1" -> {
                            int index;

                            System.out.println("informe a categoria [0: ECONOMY, 1: INTERMEDIARY, 2: EXCLUSIVE]");
                            index = sc.nextInt();

                            System.out.println(gatewayStub.getCarsByCategory(CarCategory.fromNumber(index)));
                        }
                        case "2" -> {
                            var cars = gatewayStub.getAllCars();
                            if (cars.isEmpty()) System.out.println("nao ha carros cadastrados");
                            else System.out.println(cars);
                        }
                        case "3" -> {
                            String model;

                            System.out.println("informe o modelo");
                            model = sc.nextLine();
                            var cars = gatewayStub.searchCar(model);
                            if (cars.isEmpty()) System.out.println("nao ha carros para esse modelo");
                            else System.out.println(cars);
                        }
                        case "4" -> {
                            String model, renavan;

                            System.out.println("informe o modelo");
                            model = sc.nextLine();
                            System.out.println("informe o renvan");
                            renavan = sc.nextLine();

                            var car = gatewayStub.searchCar(model, renavan);

                            if (car != null) System.out.println(car);
                            else System.out.println("carro nao encontrado");
                        }
                        case "5" -> System.out.println("qtd carros em estoque: " + gatewayStub.carsInStock());
                        case "6" -> {
                            String model, renavan;

                            System.out.println("informe o modelo");
                            model = sc.nextLine();
                            System.out.println("informe o renvan");
                            renavan = sc.nextLine();
                            var car = gatewayStub.buy(model, renavan);

                            if (car != null) System.out.println(car);
                            else System.out.println("o carro nao pode ser comprado");
                        }
                    }
                } catch (RemoteException | PermissionNotAllowedException | ResourceNotFoundException e) {
                    logger.severe(e.getClass().getName() + ": " + e.getMessage());
                }
            }
        }
    }
}
