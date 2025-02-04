package com.periferia.myapirest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.periferia.myapirest.Person.Person;
import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository <Person, Integer> {
    List<Person> findAllByOrderByNombreCompletoAsc();
    List<Person> findAllByOrderByFechaNacimientoAsc();

}
