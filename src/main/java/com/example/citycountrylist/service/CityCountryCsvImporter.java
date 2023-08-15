package com.example.citycountrylist.service;

import com.example.citycountrylist.entity.City;
import com.example.citycountrylist.entity.Country;
import com.example.citycountrylist.repository.CityRepository;
import com.example.citycountrylist.repository.CountryRepository;
import com.opencsv.CSVReaderBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CityCountryCsvImporter implements ApplicationRunner {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    @SneakyThrows
    public List<String[]> readCsvFile() {

        try (Reader fileReader = new InputStreamReader(CityCountryCsvImporter.class.getResourceAsStream("/world-cities.csv"))) {

            var csvReader = new CSVReaderBuilder(fileReader)
                    .withSkipLines(1)
                    .build();
            return csvReader.readAll();
        }
    }

    @SneakyThrows
    public void importEntitiesFromCsv() {
        List<String[]> csvData = readCsvFile();
        for (String[] row : csvData) {
            var cityName = row[0];
            var countryName = row[1];

            var country = countryRepository.findByName(countryName).orElseGet(() -> {
                var newCountry = new Country();
                newCountry.setName(countryName);
                newCountry.setLogos(getBytesFromImage(countryName));
                return countryRepository.save(newCountry);
            });

            var city = new City();
            city.setName(cityName);
            city.setCountry(country);
            cityRepository.save(city);
        }
    }

    @SneakyThrows
    public byte[] getBytesFromImage(String imageName) {
        var resource = new ClassPathResource("static/" + imageName + ".png");
        return Files.readAllBytes(resource.getFile().toPath());
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        importEntitiesFromCsv();
    }
}
