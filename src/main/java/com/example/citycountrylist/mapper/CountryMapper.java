package com.example.citycountrylist.mapper;

import com.example.citycountrylist.dto.CountryDto;
import com.example.citycountrylist.entity.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    CountryDto toCountryDto(Country country);
}
