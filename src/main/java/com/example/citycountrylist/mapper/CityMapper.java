package com.example.citycountrylist.mapper;

import com.example.citycountrylist.dto.CityDto;
import com.example.citycountrylist.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CityMapper {

    @Mapping(target = "id", source = "city.id")
    @Mapping(target = "countryDto.id", source = "city.country.id")
    @Mapping(target = "countryDto.name", source = "city.country.name")
    @Mapping(target = "countryDto.logos", source = "city.country.logos")
    CityDto toCityDto(City city);

    List<CityDto> toCityListDto(List<City> cityList);
}
