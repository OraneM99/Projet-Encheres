package com.eni.demo.ihm;

import com.eni.demo.bo.Person;
import com.eni.demo.service.PersonService;
import com.eni.demo.service.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PersonController {

    @Autowired
    PersonService personService;

    @GetMapping("/show-persons")
    public String showPersons(Model model){
        // Appeler le métier
        ServiceResponse serviceResponse = personService.getAll();

        // Le model envoie des données dans le front/requetes http/html
        model.addAttribute("persons", serviceResponse.data);

        // Affiche le front
        return "person-list.html";
    }
}
