package com.example.citycountrylist.service;

import com.example.citycountrylist.dto.CityDto;
import com.example.citycountrylist.entity.City;
import com.example.citycountrylist.mapper.CityMapper;
import com.example.citycountrylist.repository.CityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
@TestPropertySource(properties = {
        "app.pageSize=10"
})
class CityServiceTest {

    @Autowired
    private CityService cityService;

    @MockBean
    private CityRepository cityRepository;

    @MockBean
    private CityMapper cityMapper;

    private final City sampleCity = new City();

    private final CityDto sampleCityDto = new CityDto();

    @Test
    void getPaginatedCityListTest() {
        int pageNumber = 0;
        int pageSize = 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<City> cityList = List.of(sampleCity);
        Page<City> cityPage = new PageImpl<>(cityList);

        when(cityRepository.findAll(pageable)).thenReturn(cityPage);

        cityService.getPaginatedCityList(pageNumber);

        verify(cityRepository).findAll(pageable);
        verify(cityMapper, times(cityList.size())).toCityDto(any(City.class));
    }

    @Test
    void getAllCitiesByCountryNameTest() {
        String countryName = "ExampleCountry";
        List<City> cityList = new ArrayList<>();

        when(cityRepository.findAllByCountryName(countryName)).thenReturn(cityList);
        when(cityMapper.toCityListDto(cityList)).thenReturn(new ArrayList<>());

        cityService.getAllCitiesByCountryName(countryName);

        verify(cityRepository, times(1)).findAllByCountryName(countryName);
    }

    @Test
    void updateCityTest() {
        String newName = "NewCityName";

        when(cityRepository.findById(any(UUID.class))).thenReturn(Optional.of(sampleCity));
        when(cityMapper.toCityDto(sampleCity)).thenReturn(sampleCityDto);

        cityService.updateCity(UUID.randomUUID(), newName);

        verify(cityRepository).findById(any(UUID.class));
        verify(cityMapper).toCityDto(sampleCity);
    }

    @Test
    void updateNonExistentCity_ThrowEntityNotFoundException_Test() {
        UUID nonExistentCityId = UUID.randomUUID();
        String newName = "NewCityName";

        when(cityRepository.findById(nonExistentCityId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> cityService.updateCity(nonExistentCityId, newName));
    }
}
