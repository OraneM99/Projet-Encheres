package com.eni.demo;

import com.eni.demo.bo.Person;
import com.eni.demo.service.PersonService;
import com.eni.demo.service.ServiceResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	PersonService personService;

	@Test
	void contextLoads() {
	}

	// RG-001
	// Cas1 : la liste marche
	// Cas2 : la liste est vide = erreur
	@Test
	void test1(){


		ServiceResponse response = personService.getAll();

		//Assert.isTrue(response.code.equals("765"), response.message);
		Assert.isTrue(response.code.equals("200"), response.message);
	}
}
