package com.blocker147.vftask.animal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AnimalServiceTest {

    @Autowired
    private AnimalService animalService;

    @MockBean
    private AnimalRepository animalRepository;

    private Animal animal() {
        return new Animal(1L, "Animal", 1, 5);
    }

    @Test
    void findById() {
        Animal expected = animal();
        when(animalRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        Animal actual = animalService.findById(expected.getId());

        assertEquals(expected, actual);
    }

    @Test
    void findByName() {
        Animal expected = animal();
        when(animalRepository.findByName(expected.getName())).thenReturn(Optional.of(expected));
        Animal actual = animalService.findByName(expected.getName());

        assertEquals(expected, actual);
    }

    @Test
    void save() {
        Animal expected = animal();
        expected.setId(null);
        when(animalRepository.save(any(Animal.class))).thenAnswer(i -> i.getArguments()[0]);
        Animal actual = animalService.save(expected);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void findByIdShouldReturnAnimalNotFoundException() {
        Long id = -1L;
        String expected = String.format("Animal with id %d not found.", id);
        String actual = null;
        when(animalRepository.findById(id)).thenThrow(new AnimalNotFoundException(expected));
        try {
            animalService.findById(id);
        } catch (AnimalNotFoundException e) {
            actual = e.getMessage();
        }

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void findByNameShouldReturnAnimalNotFoundException() {
        String name = "zxc";
        String expected = String.format("Animal with id %s not found.", name);
        String actual = null;
        when(animalRepository.findByName(name)).thenThrow(new AnimalNotFoundException(expected));
        try {
            animalService.findByName(name);
        } catch (AnimalNotFoundException e) {
            actual = e.getMessage();
        }

        assertEquals(expected, actual);
    }
}