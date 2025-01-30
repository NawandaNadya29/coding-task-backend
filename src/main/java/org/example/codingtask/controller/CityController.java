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
                                            @RequestParam(required = false) Long longitude) {  // Changed longitude to Long

        List<City> cities;

        // Search for cities based on query 'q', if none, retrieve all cities
        if (q != null && !q.isEmpty()) {
            cities = cityService.findCities(q);
        } else {
            cities = cityService.findAllCities();
        }

        boolean useUserCoordinates = (latitude != null && longitude != null);

        for (City city : cities) {
            if (city.getLat() != null && city.getLon() != null) {  // Changed latitude and longitude to lat and lon
                double score;

                if (useUserCoordinates) {
                    score = cityService.calculateScore(city, latitude, longitude.doubleValue());  // Used doubleValue() to convert Long to Double
                } else {
                    score = cityService.calculateScore(city, city.getLat(), city.getLon());
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
                    if (city.getAdmin1() != null) {  // Changed admin1Code to admin1
                        formattedName += ", " + city.getAdmin1();  // Adding province (admin1)
                    }
                    if (city.getCountry() != null) {  // Changed countryCode to country
                        formattedName += ", " + city.getCountry();  // Adding country (country)
                    }

                    // Round the score with DecimalFormat and replace the comma with a period
                    String scoreFormatted = "0.0"; // Default value if score is null
                    if (city.getScore() != null) {
                        scoreFormatted = df.format(city.getScore()).replace(",", ".");
                    }

                    double roundedScore = Double.parseDouble(scoreFormatted);

                    // Return a CityDTO object with a rounded score
                    return new CityDTO(formattedName,
                            city.getLat(),  // Changed latitude to lat
                            city.getLon(),  // Changed longitude to lon
                            roundedScore);
                }).sorted((c1, c2) -> Double.compare(c2.getScore(), c1.getScore())).collect(Collectors.toList());

        // Sort cities by highest score

        return suggestions;
    }
}
