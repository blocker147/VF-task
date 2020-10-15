package com.blocker147.vftask.student;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnimalService {
    private final static Map<Long, Animal> animals = new HashMap<>();
    private static Long ID = 4L;

    static {
        animals.put(1L, new Animal(1L, "Name1", 10, 15));
        animals.put(2L, new Animal(2L, "Name2", 6, 25));
        animals.put(3L, new Animal(3L, "Name3", 1, 5));
    }

    public Animal findById(Long id) {
        if (!animals.containsKey(id)) throw new AnimalNotFoundException("Animal with id '" + id + "' not found.");
        return animals.get(id);
    }

    public Animal findByName(String name) {
        if (name == null) throw new NullPointerException("Name can't be null.");
        for (Animal animal : animals.values()) {
            if (name.equals(animal.getName())) return animal;
        }
        throw new AnimalNotFoundException("Animal with name '" + name + "' not found.");
    }

    public Animal save(Animal animal) {
        animal.setId(ID);
        animals.put(ID++, animal);
        return animal;
    }
}
