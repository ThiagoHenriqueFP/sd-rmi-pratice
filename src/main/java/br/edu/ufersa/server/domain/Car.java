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


    public String getModel() {
        return model;
    }

    public String getRenavan() {
        return renavan;
    }

    public CarCategory getCategory() {
        return category;
    }

    public Integer getYear() {
        return year;
    }

    public Double getPrice() {
        return price;
    }


    public void setModel(String model) {
        this.model = model;
    }

    public void setRenavan(String renavan) {
        this.renavan = renavan;
    }

    public void setCategory(CarCategory category) {
        this.category = category;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void update(Car car) {
        this.model = car.getModel();
        this.renavan = car.getRenavan();
        this.category = car.getCategory();
        this.year = car.getYear();
        this.price = car.getPrice();
    }
}
