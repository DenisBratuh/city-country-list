package com.example.citycountrylist.service;

import com.example.citycountrylist.dto.CityDto;
import com.example.citycountrylist.entity.City;
import com.example.citycountrylist.mapper.CityMapper;
import com.example.citycountrylist.repository.CityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CityService {

    @Value("${app.pageSize}")
    private int pageSize;

    private final CityRepository cityRepository;

    private final CityMapper cityMapper;

    @Transactional(readOnly = true)
    public Page<CityDto> getPaginatedCityList(int pageNumber) {
        var pageable = PageRequest.of(pageNumber, pageSize);
        return cityRepository.findAll(pageable)
                .map(cityMapper::toCityDto);
    }

    @Transactional(readOnly = true)
    public List<CityDto> getAllCitiesByCountryName(String countryName) {
        return cityMapper.toCityListDto(cityRepository.findAllByCountryName(countryName));
    }

    @Transactional
    public CityDto updateCity(UUID id, String name) {
        var city = getCityById(id);
        Optional.ofNullable(name)
                .ifPresent(city::setName);
        return cityMapper.toCityDto(city);
    }

    private City getCityById(UUID id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("City not found with id:" + id));
    }
}
