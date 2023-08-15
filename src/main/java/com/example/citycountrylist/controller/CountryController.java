package com.example.citycountrylist.controller;

import com.example.citycountrylist.dto.CountryDto;
import com.example.citycountrylist.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/countries")
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public Page<CountryDto> getPaginatedCountries(@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {
        return countryService.getPaginatedCountryList(pageNumber);
    }

    @GetMapping("/city/{cityName}")
    public ResponseEntity<CountryDto> findCountryByCityName(@PathVariable("cityName") String cityName) {
        return ResponseEntity.ok(countryService.getCountryByCityName(cityName));
    }
}
