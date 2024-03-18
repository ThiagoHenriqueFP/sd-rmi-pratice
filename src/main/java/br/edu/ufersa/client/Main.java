package br.edu.ufersa.client;

import br.edu.ufersa.server.auth.domain.User;
import br.edu.ufersa.server.gateway.GatewayRemote;
import br.edu.ufersa.server.gateway.domain.Car;
import br.edu.ufersa.server.gateway.domain.CarCategory;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GatewayRemote gatewayStub;
        try {
            Registry registry = LocateRegistry.getRegistry();
            gatewayStub = (GatewayRemote) registry.lookup("gateway");
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }

        Scanner sc = new Scanner(System.in);
        // todo pass user to server and check permissions
        User userSession;
        boolean logged = false;
        while (!logged) {
            System.out.println("informe login e senha");
            System.out.println("login: ");
            String username = sc.nextLine();
            System.out.println("senha: ");
            String password = sc.nextLine();

            Optional<User> isLogged;
            try {
                isLogged = gatewayStub.login(username, password);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            if (isLogged.isPresent()) {
                userSession = isLogged.get();
                System.out.println("login realizado");
                logged = true;
            }
            System.out.println("login incorreto, tente novamente");

        }

        String option = "";
        while (!option.equals("exit")) {
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
                    case "exit" -> {}
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

                        boolean isSuccess = gatewayStub.createCar(renavan, model, category, year, price);

                        if (isSuccess) System.out.println("carro cadastrado com sucesso");
                        else System.out.println("carro nao cadastrado, tente novamente");
                    }
                    case "2" -> {
                        String model, renavan;

                        System.out.println("informe o modelo");
                        model = sc.nextLine();
                        System.out.println("informe o renvan");
                        renavan = sc.nextLine();

                        if (gatewayStub.deleteCar(model, renavan)) System.out.println("carro deletado com sucesso");
                        else System.out.println("erro ao deletar carro");
                    }
                    case "3" -> {
                        String model;

                        System.out.println("informe o modelo");
                        model = sc.nextLine();
                        if (gatewayStub.deleteCar(model)) System.out.println("modelos removidos com sucesso");
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

                        car.ifPresentOrElse(System.out::println, () -> System.out.println("carro nao encontrado"));
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

                        System.out.println("pesquisando o carro no banco de dados");

                        Optional<Car> carAlreadyExists = gatewayStub.searchCar(model, renavan);

                        if (carAlreadyExists.isPresent()) {
                            System.out.println("ano: ");
                            year = sc.nextInt();
                            System.out.println("categoria: [0: ECONOMY, 1: INTERMEDIARY, 2: EXCLUSIVE]");
                            int categoryInteger = sc.nextInt();
                            category = CarCategory.fromNumber(categoryInteger);
                            System.out.println("valor: ");
                            price = sc.nextDouble();

                            carAlreadyExists.get().setYear(year);
                            carAlreadyExists.get().setCategory(category);
                            carAlreadyExists.get().setPrice(price);

                            gatewayStub.update(carAlreadyExists.get());

                            System.out.println("carro atualizado\n" + carAlreadyExists.get());
                        } else System.out.println("carro nao encontrado na base de dados");
                    }
                    case "9" -> System.out.println("qtd carros em estoque: " + gatewayStub.carsInStock());
                    case "10" -> { String model, renavan;

                        System.out.println("informe o modelo");
                        model = sc.nextLine();
                        System.out.println("informe o renvan");
                        renavan = sc.nextLine();
                        var car = gatewayStub.buy(model, renavan);
                        car.ifPresentOrElse(System.out::println, () -> System.out.println("o carro nao pode ser comprado"));
                    }
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
