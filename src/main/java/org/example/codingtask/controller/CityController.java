package org.example.codingtask.controller;

import org.example.codingtask.model.City;
import org.example.codingtask.model.CityDTO;
import org.example.codingtask.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.text.DecimalFormat;

@RestController
public class CityController {

    @Autowired
    private CityService cityService;

    // Endpoint to get a list of cities with search query 'q' (or all cities if q is not given)
    @GetMapping("/suggestions")
    public List<CityDTO> getCitySuggestions(@RequestParam(required = false) String q,
                                            @RequestParam(required = false) Double latitude,
                                            @RequestParam(required = false) Double longitude) {

        List<City> cities;

        // Search for cities based on query 'q', if none, retrieve all cities
        if (q != null && !q.isEmpty()) {
            cities = cityService.findCities(q);
        } else {
            cities = cityService.findAllCities();
        }

        boolean useUserCoordinates = (latitude != null && longitude != null);

        for (City city : cities) {
            if (city.getLatitude() != null && city.getLongitude() != null) {
                double score;

                if (useUserCoordinates) {
                    score = cityService.calculateScore(city, latitude, longitude);
                } else {
                    score = cityService.calculateScore(city, city.getLatitude(), city.getLongitude());
                }

                city.setScore(score);
            }
        }

        // Create a DecimalFormat object to round numbers to one decimal number
        DecimalFormat df = new DecimalFormat("#.#");

        List<CityDTO> suggestions = cities.stream()
                .map(city -> {
                    // City name format with province and country
                    String formattedName = city.getName();
                    if (city.getAdmin1Code() != null) {
                        formattedName += ", " + city.getAdmin1Code();  // Menambahkan provinsi (admin1Code)
                    }
                    if (city.getCountryCode() != null) {
                        formattedName += ", " + city.getCountryCode();  // Menambahkan negara (countryCode)
                    }

                    // Round the score with DecimalFormat and replace the comma with a period
                    String scoreFormatted = "0.0"; // Default value if score is null
                    if (city.getScore() != null) {
                        scoreFormatted = df.format(city.getScore()).replace(",", ".");
                    }

                    double roundedScore = Double.parseDouble(scoreFormatted);

                    // Return a CityDTO object with a rounded score
                    return new CityDTO(formattedName,
                            city.getLatitude(),
                            city.getLongitude(),
                            roundedScore);
                }).sorted((c1, c2) -> Double.compare(c2.getScore(), c1.getScore())).collect(Collectors.toList());

        // Sort cities by highest score

        return suggestions;
    }
}
