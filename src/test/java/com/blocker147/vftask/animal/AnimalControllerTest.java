package com.blocker147.vftask.animal;

import com.blocker147.vftask.exception.ExceptionHandlerResponse;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AnimalController.class)
class AnimalControllerTest {
    private static final String url = "/animals";
    private static final Gson gson = new Gson();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalService animalService;

    @MockBean
    private ExceptionHandlerResponse exceptionHandlerResponse;

    private Animal animal() {
        return new Animal(1L, "Name1", 10, 25);
    }

    private Animal incorrectAnimal() {
        return new Animal(1L, "a", -1, -1);
    }

    @Test
    void findById() throws Exception {
        Animal animal = animal();
        when(animalService.findById(animal.getId())).thenReturn(animal);

        this.mockMvc.perform(get(url + "/" + animal.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(animal.getId()))
                .andExpect(jsonPath("$.name").value(animal.getName()))
                .andExpect(jsonPath("$.age").value(animal.getAge()))
                .andExpect(jsonPath("$.weight").value(animal.getWeight()));
    }

    @Test
    void findByName() throws Exception {
        Animal animal = animal();
        when(animalService.findByName(animal.getName())).thenReturn(animal);

        this.mockMvc.perform(get(url + "?name=" + animal.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(animal.getId()))
                .andExpect(jsonPath("$.name").value(animal.getName()))
                .andExpect(jsonPath("$.age").value(animal.getAge()))
                .andExpect(jsonPath("$.weight").value(animal.getWeight()));
    }

    @Test
    void save() throws Exception {
        Animal animalWithoutId = animal();
        animalWithoutId.setId(null);
        when(animalService.save(any(Animal.class))).thenAnswer(i -> i.getArguments()[0]);
        String json = gson.toJson(animalWithoutId, Animal.class);

        this.mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(animalWithoutId.getName()))
                .andExpect(jsonPath("$.age").value(animalWithoutId.getAge()))
                .andExpect(jsonPath("$.weight").value(animalWithoutId.getWeight()));
    }

    @Test
    void findByIdShouldReturnAnimalNotFoundException() throws Exception {
        Long id = -1L;
        String errorMessage = String.format("Animal with id %d not found.", id);
        when(animalService.findById(id)).thenThrow(new AnimalNotFoundException(errorMessage));

        this.mockMvc.perform(get(url + "/" + id))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AnimalNotFoundException))
                .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void findByNameShouldReturnAnimalNotFoundException() throws Exception {
        String name = "zxc";
        String errorMessage = String.format("Animal with name %s not found.", name);
        when(animalService.findByName(name)).thenThrow(new AnimalNotFoundException(errorMessage));

        this.mockMvc.perform(get(url + "?name=" + name))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AnimalNotFoundException))
                .andExpect(result -> assertEquals(errorMessage, Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    void saveAnimalShouldReturnMethodArgumentNotValidException() throws Exception {
        when(animalService.save(any(Animal.class))).thenAnswer(i -> i.getArguments()[0]);
        String json = gson.toJson(incorrectAnimal(), Animal.class);

        this.mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
}
