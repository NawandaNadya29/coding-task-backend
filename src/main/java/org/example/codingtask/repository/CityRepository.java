package org.example.codingtask.repository;

import org.example.codingtask.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {

    List<City> findByNameContainingIgnoreCase(String q);
    long countByNameContainingIgnoreCase(String q);
}
