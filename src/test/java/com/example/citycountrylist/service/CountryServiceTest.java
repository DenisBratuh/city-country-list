package com.example.citycountrylist.service;

import com.example.citycountrylist.dto.CountryDto;
import com.example.citycountrylist.entity.Country;
import com.example.citycountrylist.mapper.CountryMapper;
import com.example.citycountrylist.repository.CountryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(properties = {
        "app.pageSize=10"
})
class CountryServiceTest {

    @Autowired
    private CountryService countryService;

    @MockBean
    private CountryRepository countryRepository;

    @MockBean
    private CountryMapper countryMapper;

    private final UUID sampleCityId = UUID.randomUUID();
    private final Country sampleCountry = new Country();
    private final CountryDto sampleCountryDto = new CountryDto();

    @Test
    void getPaginatedCountryListTest() {
        int pageNumber = 0;

        Pageable pageable = PageRequest.of(pageNumber, 10);
        Page<Country> countryPage = new PageImpl<>(List.of(sampleCountry));

        when(countryRepository.findAll(pageable)).thenReturn(countryPage);
        when(countryMapper.toCountryDto(sampleCountry)).thenReturn(sampleCountryDto);

        Page<CountryDto> result = countryService.getPaginatedCountryList(pageNumber);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(sampleCountryDto, result.getContent().get(0));
    }

    @Test
    void getCountryByCityNameTest() {
        String cityName = "TestCity";

        when(countryRepository.findAllByCityListName(cityName)).thenReturn(Optional.of(sampleCountry));
        when(countryMapper.toCountryDto(sampleCountry)).thenReturn(sampleCountryDto);

        CountryDto result = countryService.getCountryByCityName(cityName);

        assertNotNull(result);
        assertEquals(sampleCountryDto, result);
    }

    @Test
    void getCountryByCityNameNotFound() {
        String cityName = "TestCity";

        when(countryRepository.findAllByCityListName(cityName)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> countryService.getCountryByCityName(cityName));
    }

    @Test
    void updateCountryLogosTest() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "logos",
                "test-image.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[]{1, 2, 3, 4});

        when(countryRepository.findByCityListId(sampleCityId)).thenReturn(Optional.of(sampleCountry));

        countryService.updateCountryLogos(mockFile, sampleCityId);

        verify(countryRepository).findByCityListId(sampleCityId);
        assertNotNull(sampleCountry.getLogos());
    }

    @Test
    void updateCountryLogosWithWrongFileType() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "logos",
                "test-document.txt",
                MediaType.TEXT_PLAIN_VALUE,
                new byte[]{1, 2, 3, 4});

        when(countryRepository.findByCityListId(sampleCityId)).thenReturn(Optional.of(sampleCountry));

        assertThrows(IllegalArgumentException.class, () -> countryService.updateCountryLogos(mockFile, sampleCityId));
    }
}
