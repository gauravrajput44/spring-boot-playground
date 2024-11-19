package com.example.myframework.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Person {
    private Long id;
    private String name;
    private int age;

    // Constructors, getters, and setters
    public Person(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

}
