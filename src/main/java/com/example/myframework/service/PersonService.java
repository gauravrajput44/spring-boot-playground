package com.example.myframework.service;

import com.example.myframework.dto.CreatePersonDTO;
import com.example.myframework.dto.PersonDTO;
import com.example.myframework.exception.PersonNotFoundException;
import com.example.myframework.mapper.PersonMapper;
import com.example.myframework.model.Person;
import com.example.myframework.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonService(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    public List<PersonDTO> getAllPersons() {
        return personRepository.findAll().stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO getPersonById(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person not found with id: " + id));
        return personMapper.toDTO(person);
    }

    public PersonDTO createPerson(CreatePersonDTO createPersonDTO) {
        Person person = personMapper.toEntity(createPersonDTO);
        Person savedPerson = personRepository.save(person);
        return personMapper.toDTO(savedPerson);
    }

    public PersonDTO updatePerson(Long id, PersonDTO personDTO) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person not found with id: " + id));

        personMapper.updateEntityFromDTO(personDTO, person);
        Person updatedPerson = personRepository.save(person);
        return personMapper.toDTO(updatedPerson);
    }

    public void deletePerson(Long id) {
        if (!personRepository.existsById(id)) {
            throw new PersonNotFoundException("Person not found with id: " + id);
        }
        personRepository.deleteById(id);
    }
}