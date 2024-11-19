package com.example.myframework.controller;

import com.example.myframework.model.Person;
import com.example.myframework.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("/{id}")
    public Person getPersonById(@PathVariable Long id) {
        Optional<Person> person = personService.getPersonById(id);
        return person.orElse(null); // Return null if not found
    }

    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        return personService.addPerson(person);
    }

    @PutMapping("/{id}")
    public Person updatePerson(@PathVariable Long id, @RequestBody Person person) {
        return personService.updatePerson(id, person);
    }

    @DeleteMapping("/{id}")
    public boolean deletePerson(@PathVariable Long id) {
        return personService.deletePerson(id);
    }
}
