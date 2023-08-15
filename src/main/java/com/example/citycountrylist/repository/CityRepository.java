package com.example.citycountrylist.repository;

import com.example.citycountrylist.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CityRepository extends JpaRepository<City, UUID> {

    List<City> findAllByCountryName(String countryName);

    Optional<City> findById(UUID id);
}
