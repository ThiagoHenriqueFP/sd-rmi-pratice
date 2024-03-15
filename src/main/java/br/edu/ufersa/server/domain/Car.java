package br.edu.ufersa.server.domain;

import java.io.Serializable;

public class Car implements Serializable {
    private String model;
    private String renavan;
    private CarCategory category;
    private Integer year;
    private Double price;

    public Car(String model, CarCategory category, String renavan, Integer year, Double price) {
        this.model = model;
        this.renavan = renavan;
        this.category = category;
        this.year = year;
        this.price = price;
    }
}
