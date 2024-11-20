package com.example.myframework.controller;

import com.example.myframework.controller.config.TestConfig;
import com.example.myframework.dto.CreatePersonDTO;
import com.example.myframework.dto.PersonDTO;
import com.example.myframework.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestConfig.class)
class PersonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
    }

    @Test
    void createPerson_ValidInput_ReturnsCreatedPerson() throws Exception {
        CreatePersonDTO createPersonDTO = new CreatePersonDTO();
        createPersonDTO.setName("John Doe");
        createPersonDTO.setAge(30);

        mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPersonDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void createPerson_InvalidInput_ReturnsBadRequest() throws Exception {
        CreatePersonDTO createPersonDTO = new CreatePersonDTO();
        createPersonDTO.setName("");
        createPersonDTO.setAge(-1);

        mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPersonDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Name is required"))
                .andExpect(jsonPath("$.age").value("Age must be greater than or equal to 0"));
    }

    @Test
    void getAllPersons_ReturnsAllPersons() throws Exception {
        // Create test persons
        CreatePersonDTO person1 = new CreatePersonDTO();
        person1.setName("John");
        person1.setAge(25);

        CreatePersonDTO person2 = new CreatePersonDTO();
        person2.setName("Jane");
        person2.setAge(30);

        mockMvc.perform(post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person1)));
        mockMvc.perform(post("/api/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person2)));

        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("John", "Jane")))
                .andExpect(jsonPath("$[*].age", containsInAnyOrder(25, 30)));
    }

    @Test
    void getPersonById_ExistingId_ReturnsPerson() throws Exception {
        // Create test person
        CreatePersonDTO createPersonDTO = new CreatePersonDTO();
        createPersonDTO.setName("John");
        createPersonDTO.setAge(25);

        String response = mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPersonDTO)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PersonDTO createdPerson = objectMapper.readValue(response, PersonDTO.class);

        mockMvc.perform(get("/api/persons/{id}", createdPerson.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void getPersonById_NonExistingId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/persons/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Person not found with id: 999"));
    }

    @Test
    void updatePerson_ValidInput_ReturnsUpdatedPerson() throws Exception {
        // Create test person
        CreatePersonDTO createPersonDTO = new CreatePersonDTO();
        createPersonDTO.setName("John");
        createPersonDTO.setAge(25);

        String response = mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPersonDTO)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PersonDTO createdPerson = objectMapper.readValue(response, PersonDTO.class);

        // Update person
        createdPerson.setName("John Updated");
        createdPerson.setAge(26);

        mockMvc.perform(put("/api/persons/{id}", createdPerson.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdPerson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.age").value(26));
    }

    @Test
    void deletePerson_ExistingId_ReturnsNoContent() throws Exception {
        // Create test person
        CreatePersonDTO createPersonDTO = new CreatePersonDTO();
        createPersonDTO.setName("John");
        createPersonDTO.setAge(25);

        String response = mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPersonDTO)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PersonDTO createdPerson = objectMapper.readValue(response, PersonDTO.class);

        mockMvc.perform(delete("/api/persons/{id}", createdPerson.getId()))
                .andExpect(status().isNoContent());

        // Verify person is deleted
        mockMvc.perform(get("/api/persons/{id}", createdPerson.getId()))
                .andExpect(status().isNotFound());
    }
}