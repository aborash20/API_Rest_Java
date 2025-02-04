Documento Técnico: API REST de Gestión de Clientes en Java (Spring Boot)
1. Introducción
Este documento técnico describe una API REST construida en Java utilizando el framework Spring Boot. La API está diseñada para gestionar clientes, permitiendo guardar su información (nombre completo, documento de identidad, correo electrónico, fecha de nacimiento y zona horaria local), listar los clientes ordenados alfabéticamente, obtener un listado de clientes ordenados por edad y calcular la cantidad de clientes junto con el promedio de edad.

2. Tecnologías Utilizadas
Java 17: Lenguaje de programación utilizado para desarrollar la API.
Spring Boot 2.x: Framework que permite crear aplicaciones Java de manera rápida y sencilla.
Spring Data JPA: Para facilitar la interacción con la base de datos utilizando JPA (Java Persistence API).
H2 Database: Base de datos en memoria para almacenar los datos de los clientes durante la ejecución del proyecto.
Maven: Herramienta de gestión de dependencias y construcción del proyecto.
Postman: Herramienta para probar los endpoints de la API.

3. Estructura del Proyecto
La estructura del proyecto es la siguiente:

src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── demo/
│   │               ├── controller/
│   │               │   └── ClienteController.java
│   │               ├── model/
│   │               │   └── Cliente.java
│   │               ├── repository/
│   │               │   └── ClienteRepository.java
│   │               ├── service/
│   │               │   └── ClienteService.java
│   │               └── DemoApplication.java
│   ├── resources/
│   │   └── application.properties
└── pom.xml
4. Descripción de Componentes
4.1. Modelo - Cliente.java
La clase Cliente representa la entidad Cliente, que será mapeada a la tabla de la base de datos. Esta clase tiene los campos necesarios para almacenar la información del cliente, y se usa junto con JPA para persistir los datos.

package com.example.demo.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCompleto;
    private String documentoIdentidad;
    private String correoElectronico;
    private LocalDate fechaNacimiento;
    private String zonaHorariaLocal;

    // Getters y setters
}
4.2. Repositorio - ClienteRepository.java
El repositorio se encarga de interactuar con la base de datos. Extiende JpaRepository, que proporciona métodos básicos para acceder y manipular los datos sin necesidad de escribir consultas SQL.

package com.example.demo.repository;

import com.example.demo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Métodos de consulta adicionales si es necesario
}
4.3. Servicio - ClienteService.java
El servicio contiene la lógica de negocio de la aplicación, como crear un cliente, obtener clientes ordenados alfabéticamente o por edad, y calcular la cantidad y el promedio de edad.

package com.example.demo.service;

import com.example.demo.model.Cliente;
import com.example.demo.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public List<Cliente> obtenerClientesOrdenadosAlfabeticamente() {
        return clienteRepository.findAllByOrderByNombreCompletoAsc();
    }

    public List<String> obtenerClientesOrdenadosPorEdad() {
        List<Cliente> clientes = clienteRepository.findAllByOrderByFechaNacimientoAsc();
        return clientes.stream()
                .map(cliente -> {
                    int edad = Period.between(cliente.getFechaNacimiento(), LocalDate.now()).getYears();
                    return cliente.getNombreCompleto() + " - Edad: " + edad;
                })
                .collect(Collectors.toList());
    }

    public String obtenerCantidadYPromedioEdad() {
        List<Cliente> clientes = clienteRepository.findAll();
        int cantidad = clientes.size();
        double promedioEdad = clientes.stream()
                .mapToInt(cliente -> Period.between(cliente.getFechaNacimiento(), LocalDate.now()).getYears())
                .average()
                .orElse(0);
        return "Cantidad de clientes: " + cantidad + ", Promedio de edad: " + promedioEdad;
    }
}
4.4. Controlador - ClienteController.java
El controlador expone los endpoints de la API REST que permiten al cliente interactuar con el servicio. A través de las rutas definidas, los usuarios pueden enviar peticiones HTTP para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar).

package com.example.demo.controller;

import com.example.demo.model.Cliente;
import com.example.demo.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public Cliente createCliente(@RequestBody Cliente cliente) {
        return clienteService.crearCliente(cliente);
    }

    @GetMapping("/listado-alfabetico")
    public List<Cliente> getClientesAlfabetico() {
        return clienteService.obtenerClientesOrdenadosAlfabeticamente();
    }

    @GetMapping("/listado-por-edad")
    public List<String> getClientesPorEdad() {
        return clienteService.obtenerClientesOrdenadosPorEdad();
    }

    @GetMapping("/cantidad-promedio")
    public String getCantidadYPromedioEdad() {
        return clienteService.obtenerCantidadYPromedioEdad();
    }
}
4.5. Aplicación Principal - DemoApplication.java
La clase principal que inicia la aplicación Spring Boot. Esta clase contiene el método main, que arranca el servidor.

package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
5. Configuración de la Base de Datos
La base de datos utilizada es H2, que es una base de datos en memoria. Para configurar esta base de datos, se editó el archivo application.properties de la siguiente manera:

# Configuración de la base de datos H2
spring.datasource.url=jdbc:h2:mem:myapirestdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=root
spring.datasource.password=1234
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
6. Pruebas de la API
Se pueden utilizar herramientas como Postman o cURL para probar los siguientes endpoints:

Crear un cliente:

Método: POST
URL: http://localhost:8080/clientes
Body (JSON):
json
Copiar
{
  "nombreCompleto": "Juan Pérez",
  "documentoIdentidad": "12345678",
  "correoElectronico": "juan.perez@email.com",
  "fechaNacimiento": "1990-01-01",
  "zonaHorariaLocal": "America/Argentina/Buenos_Aires"
}
Obtener clientes ordenados alfabéticamente:

Método: GET
URL: http://localhost:8080/clientes/listado-alfabetico
Obtener clientes ordenados por edad:

Método: GET
URL: http://localhost:8080/clientes/listado-por-edad
Obtener cantidad de clientes y promedio de edad:

Método: GET
URL: http://localhost:8080/clientes/cantidad-promedio

7. Conclusión
Este proyecto proporciona una API REST sencilla para gestionar clientes. Utiliza Spring Boot para el desarrollo de la API, Spring Data JPA para manejar la persistencia de los datos y H2 como base de datos en memoria. La arquitectura es fácil de mantener y permite expandir la funcionalidad con facilidad.
