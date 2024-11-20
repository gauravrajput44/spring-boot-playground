package com.example.myframework.mapper;

import com.example.myframework.dto.CreatePersonDTO;
import com.example.myframework.dto.PersonDTO;
import com.example.myframework.model.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public PersonDTO toDTO(Person person) {
        PersonDTO dto = new PersonDTO();
        dto.setId(person.getId());
        dto.setName(person.getName());
        dto.setAge(person.getAge());
        return dto;
    }

    public Person toEntity(CreatePersonDTO dto) {
        Person person = new Person();
        person.setName(dto.getName());
        person.setAge(dto.getAge());
        return person;
    }

    public void updateEntityFromDTO(PersonDTO dto, Person person) {
        person.setName(dto.getName());
        person.setAge(dto.getAge());
    }
}