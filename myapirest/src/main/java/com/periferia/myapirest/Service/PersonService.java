package com.periferia.myapirest.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.periferia.myapirest.Person.Person;
import com.periferia.myapirest.repository.PersonRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    // Método para crear un cliente
    public Person crearCliente(Person person) {
        return personRepository.save(person);
    }

    // Método para obtener listado de clientes ordenados alfabéticamente
    public List<Person> obtenerClientesOrdenadosAlfabeticamente() {
        return personRepository.findAllByOrderByNombreCompletoAsc();
    }

    // Método para obtener listado de clientes ordenados por edad
    public List<String> obtenerClientesOrdenadosPorEdad() {
        List<Person> persons = personRepository.findAllByOrderByFechaNacimientoAsc();
        return persons.stream()
                .map(person -> {
                    int edad = Period.between(person.getFechaNacimiento(), LocalDate.now()).getYears();
                    return person.getNombreCompleto() + " - Edad: " + edad;
                })
                .collect(Collectors.toList());
    }

    // Método para obtener la cantidad de clientes y el promedio de edad
    public String obtenerCantidadYPromedioEdad() {
        List<Person> clientes = personRepository.findAll();
        int cantidad = clientes.size();
        double promedioEdad = clientes.stream()
                .mapToInt(cliente -> Period.between(cliente.getFechaNacimiento(), LocalDate.now()).getYears())
                .average()
                .orElse(0);
        return "Cantidad de clientes: " + cantidad + ", Promedio de edad: " + promedioEdad;
    }
}