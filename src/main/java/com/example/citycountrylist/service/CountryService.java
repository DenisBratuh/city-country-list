package com.example.citycountrylist.service;

import com.example.citycountrylist.dto.CountryDto;
import com.example.citycountrylist.entity.Country;
import com.example.citycountrylist.mapper.CountryMapper;
import com.example.citycountrylist.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CountryService {

    @Value("${app.pageSize}")
    private int pageSize;

    private final CountryRepository countryRepository;

    private final CountryMapper countryMapper;

    @Transactional(readOnly = true)
    public Page<CountryDto> getPaginatedCountryList(int pageNumber) {
        var pageable = PageRequest.of(pageNumber, pageSize);
        return countryRepository.findAll(pageable)
                .map(countryMapper::toCountryDto);
    }

    @Transactional(readOnly = true)
    public CountryDto getCountryByCityName(String cityName) {
        return countryMapper.toCountryDto(countryRepository.findAllByCityListName(cityName)
                .orElseThrow(() -> new EntityNotFoundException("Country not found with city name :" + cityName)));
    }

    @Transactional
    public void updateCountryLogos(MultipartFile logos, UUID cityId) {
        var country = getCountryByCityId(cityId);
                Optional.ofNullable(logos).ifPresent(multipartFile -> country.setLogos(convertMultipartFileToBytes(multipartFile)));
    }

    private Country getCountryByCityId(UUID cityId) {
        return countryRepository.findByCityListId(cityId)
                .orElseThrow(() -> new EntityNotFoundException("Country not found with cityId:" + cityId));
    }

    private void validateImageType(MultipartFile file) {
        var contentType = file.getContentType();
        if (contentType != null && !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Wrong file format! Only Pictures allowed!");
        }
    }

    @SneakyThrows
    private byte[] convertMultipartFileToBytes(MultipartFile file) {
        validateImageType(file);
        return file.getBytes();
    }
}
