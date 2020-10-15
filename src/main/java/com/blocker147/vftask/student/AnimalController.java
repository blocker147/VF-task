package com.blocker147.vftask.student;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/animals")
public class AnimalController {
    private static final Logger log = LoggerFactory.getLogger(AnimalController.class);
    private static final String controller = "'/animals";
    @Autowired
    private AnimalService animalService;

    @GetMapping("/{id}")
    public Animal findById(@PathVariable Long id) {
        log.info(controller + "/" + id + "' findByID() with path variable id ' " + id + " '.");
        return animalService.findById(id);
    }

    @GetMapping
    public Animal findByName(@RequestParam(required = false) String name) {
        log.info(controller + "?name=" + name + "' findByName() with parameter name '" + name + "'.");
        return animalService.findByName(name);
    }

    @PostMapping
    public Animal save(@Valid @RequestBody Animal animal) {
        log.info(controller + "' save() with body '" + animal + "'.");
        return animalService.save(animal);
    }
}
