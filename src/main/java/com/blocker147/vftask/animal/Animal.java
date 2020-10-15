package com.blocker147.vftask.animal;

import javax.validation.constraints.*;
import java.util.Objects;

public class Animal {

    @Null(message = "Do not provide id.")
    private Long id;

    @NotNull(message = "Name can't be null.")
    @Size(min = 2, max = 20, message = "Name length must be between 2 and 20 characters long.")
    private String name;

    @NotNull(message = "Age can't be null.")
    @Max(value = 100, message = "Age must be between of 0 and 100.")
    private int age;

    @NotNull(message = "Weight can't be null.")
    @Max(value = 500, message = "Weight must be between 1 and 500.")
    @Min(value = 1, message = "Weight must be between 1 and 500.")
    private int weight;

    public Animal() {
    }

    public Animal(Long id, String name, int age, int weight) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return Objects.equals(id, animal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                '}';
    }
}
