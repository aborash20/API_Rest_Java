package com.periferia.myapirest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.periferia.myapirest.Person.Person;
import com.periferia.myapirest.Service.PersonService;

@RestController
@RequestMapping("/clientes")
public class PersonController {

    @Autowired
    private PersonService personService;

    // Crear cliente
    @PostMapping
    public Person createCliente(@RequestBody Person person) {
        return personService.crearCliente(person);
    }

    @GetMapping
    public List<Person> getAllClientes() {
        return personService.obtenerClientesOrdenadosAlfabeticamente(); // O ajusta según el criterio que necesites
    }

    // Obtener clientes ordenados alfabéticamente
    @GetMapping("/listado-alfabetico")
    public List<Person> getClientesAlfabetico() {
        return personService.obtenerClientesOrdenadosAlfabeticamente();
    }

    // Obtener clientes ordenados por edad
    @GetMapping("/listado-por-edad")
    public List<String> getClientesPorEdad() {
        return personService.obtenerClientesOrdenadosPorEdad();
    }

    // Obtener cantidad de clientes y promedio de edad
    @GetMapping("/cantidad-promedio")
    public String getCantidadYPromedioEdad() {
        return personService.obtenerCantidadYPromedioEdad();
    }
}
