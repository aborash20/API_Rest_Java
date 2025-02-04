package com.periferia.myapirest.Person;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Person {
    @Id
    @GeneratedValue
    private Integer id;
    @Basic
    private String nombreCompleto;
    private String documentoIdentidad;
    private String correoElectronico;
    private LocalDate fechaNacimiento;
    private ZonedDateTime zonaHorariaLocal;

}
