package com.blocker147.vftask.animal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository animalRepository;

    public Animal findById(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new AnimalNotFoundException("Animal with id '" + id + "' not found."));
    }

    public Animal findByName(String name) {
        if (name == null) throw new NullPointerException("Name can't be null.");
        return animalRepository.findByName(name)
                .orElseThrow(() -> new AnimalNotFoundException("Animal with name '" + name + "' not found."));
    }

    public Animal save(Animal animal) {
        return animalRepository.save(animal);
    }
}
