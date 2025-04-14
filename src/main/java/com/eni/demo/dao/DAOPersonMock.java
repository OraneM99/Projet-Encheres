package com.eni.demo.dao;

import com.eni.demo.bo.Person;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DAOPersonMock implements IDAOPerson {

    public List<Person> persons = new ArrayList<Person>();

    public DAOPersonMock(){
        persons.add(new Person(1L, "Toto"));
        persons.add(new Person(2L, "Chocolatine"));
        persons.add(new Person(3L, "Beurre Doux"));
    }

    @Override
    public List<Person> selectAll() {
        return persons;
    }

}
