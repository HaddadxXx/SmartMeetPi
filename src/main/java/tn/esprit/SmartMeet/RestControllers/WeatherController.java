package tn.esprit.SmartMeet.RestControllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "http://localhost:4200")
public class WeatherController {

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private final String apiUrl = "https://api.openweathermap.org/data/2.5";
    private final RestTemplate restTemplate;

    public WeatherController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentWeather(
            @RequestParam String city,
            @RequestParam(defaultValue = "metric") String unit) {
        String url = String.format("%s/weather?q=%s&appid=%s&units=%s", apiUrl, city, apiKey, unit);
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error fetching weather data: " + e.getMessage());
        }
    }

    @GetMapping("/forecast")
    public ResponseEntity<?> getWeatherForecast(
            @RequestParam String city,
            @RequestParam(defaultValue = "metric") String unit) {
        String url = String.format("%s/forecast?q=%s&appid=%s&units=%s", apiUrl, city, apiKey, unit);
        try {
            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
            return ResponseEntity.ok(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error fetching forecast data: " + e.getMessage());
        }
    }
}