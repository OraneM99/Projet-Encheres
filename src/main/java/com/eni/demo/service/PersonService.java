package com.eni.demo.service;

import com.eni.demo.bo.Person;
import com.eni.demo.dao.IDAOPerson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.eni.demo.service.ServiceConstant.CD_SUCCESS;

@Service
public class PersonService {

    @Autowired
    IDAOPerson daoPerson;

    /**
     * RG-001 : Récupérer les personnes du site
     * @return
     */
    public ServiceResponse<List<Person>> getAll() {
        List<Person> persons = daoPerson.selectAll();

        // Erreur : 765 (Liste vide)
        if (persons.isEmpty()){

            return ServiceResponse.buildResponse("765", "Liste invalide", persons);
        }

        // Success : 200
        return ServiceResponse.buildResponse(CD_SUCCESS, "La liste des personnes a été récupérée avec succès", persons);
    }
}
