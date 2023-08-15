package com.example.citycountrylist.repository;

import com.example.citycountrylist.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<Country, UUID> {

    Optional<Country> findAllByCityListName(String name);

    Optional<Country> findByName(String name);

    Optional<Country> findByCityListId(UUID id);
}
