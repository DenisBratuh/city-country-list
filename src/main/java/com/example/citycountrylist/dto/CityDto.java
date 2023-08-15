package com.example.citycountrylist.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CityDto {

    private UUID id;

    private String name;

    CountryDto countryDto;
}
