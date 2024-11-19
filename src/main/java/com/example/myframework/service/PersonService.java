package com.example.myframework.service;

import com.example.myframework.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private List<Person> persons = new ArrayList<>();

    public PersonService() {
        // Add some sample data
        persons.add(new Person(1L, "Alice", 30));
        persons.add(new Person(2L, "Bob", 25));
    }

    public List<Person> getAllPersons() {
        return persons;
    }

    public Optional<Person> getPersonById(Long id) {
        return persons.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public Person addPerson(Person person) {
        persons.add(person);
        return person;
    }

    public Person updatePerson(Long id, Person person) {
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getId().equals(id)) {
                persons.set(i, person);
                return person;
            }
        }
        return null;
    }

    public boolean deletePerson(Long id) {
        return persons.removeIf(p -> p.getId().equals(id));
    }
}
