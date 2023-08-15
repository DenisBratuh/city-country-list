package com.example.citycountrylist.controller;

import com.example.citycountrylist.dto.CityDto;
import com.example.citycountrylist.service.CityService;
import com.example.citycountrylist.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    private final CountryService countryService;

    @GetMapping
    public Page<CityDto> getPaginatedCityList(@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber) {
        return cityService.getPaginatedCityList(pageNumber);
    }

    @GetMapping("/country/{countryName}")
    public List<CityDto> getCitiesByCountryName(@PathVariable("countryName") String countryName) {
        return cityService.getAllCitiesByCountryName(countryName);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('EDITOR')")
    public CityDto editCity(@PathVariable("id") UUID id,
                            @RequestParam String name,
                            @RequestParam(required = false) MultipartFile logo) {
        countryService.updateCountryLogos(logo, id);
        return cityService.updateCity(id, name);
    }
}
