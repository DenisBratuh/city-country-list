package com.example.citycountrylist.controller;

import com.example.citycountrylist.dto.CityDto;
import com.example.citycountrylist.service.CityService;
import com.example.citycountrylist.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CityController.class)
@AutoConfigureMockMvc(addFilters = false)
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityService cityService;

    @MockBean
    private CountryService countryService;

    @Test
    void getPaginatedCityListTest() throws Exception {
        Page<CityDto> mockPage = new PageImpl<>(List.of(new CityDto()));

        when(cityService.getPaginatedCityList(anyInt())).thenReturn(mockPage);

        mockMvc.perform(get("/cities?pageNumber=0"))
                .andExpect(status().isOk());

        verify(cityService).getPaginatedCityList(anyInt());
    }

    @Test
    void testGetCitiesByCountryName() throws Exception {
        when(cityService.getAllCitiesByCountryName(any())).thenReturn(List.of(new CityDto()));

        mockMvc.perform(get("/cities/country/someCountry"))
                .andExpect(status().isOk());

        verify(cityService).getAllCitiesByCountryName(any(String.class));
    }

    @Test
    @WithMockUser(roles = "EDITOR")
    void testEditCity() throws Exception {
        CityDto mockCityDto = new CityDto();
        when(cityService.updateCity(any(), any())).thenReturn(mockCityDto);

        MockMultipartFile file = new MockMultipartFile("logo", "test.jpg", "image/jpeg", "some-image".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/cities/" + UUID.randomUUID())
                        .file(file)
                        .param("name", "someName")
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk());

        verify(countryService).updateCountryLogos(any(MockMultipartFile.class), any(UUID.class));
        verify(cityService).updateCity(any(UUID.class), any(String.class));
    }
}
