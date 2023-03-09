package ru.evgenii.WeatherDataCollectorREST.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class SensorDTO {
    @NotEmpty(message = "Name should not be empty; ")
    @Size(min = 3, max = 30, message = "The name must contain from 3 to 30 characters; ")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
