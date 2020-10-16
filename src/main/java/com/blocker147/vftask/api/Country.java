package com.blocker147.vftask.api;

public class Country {
    private String capital;
    private int population;

    public Country() {
    }

    public Country(String capital, int population) {
        this.capital = capital;
        this.population = population;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    @Override
    public String toString() {
        return "Country{" +
                "capital='" + capital + '\'' +
                ", population=" + population +
                '}';
    }
}
