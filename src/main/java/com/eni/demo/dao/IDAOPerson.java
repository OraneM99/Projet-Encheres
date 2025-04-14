package com.eni.demo.dao;

import com.eni.demo.bo.Person;

import java.util.List;

public interface IDAOPerson {

    List<Person> selectAll();
}
