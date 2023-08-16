package com.example.citycountrylist.controller;

import com.example.citycountrylist.dto.CountryDto;
import com.example.citycountrylist.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CountryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    @Test
    void getPaginatedCountriesTest() throws Exception {
        Page<CountryDto> mockPage = new PageImpl<>(List.of(new CountryDto()));

        when(countryService.getPaginatedCountryList(anyInt())).thenReturn(mockPage);

        mockMvc.perform(get("/countries?pageNumber=0"))
                .andExpect(status().isOk());

        verify(countryService).getPaginatedCountryList(anyInt());
    }

    @Test
    void testFindCountryByCityNameTest() throws Exception {
        String cityName = "ExampleCity";
        CountryDto mockCountryDto = new CountryDto();
        when(countryService.getCountryByCityName(cityName)).thenReturn(mockCountryDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/countries/city/" + cityName))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(countryService).getCountryByCityName(cityName);
    }
}
