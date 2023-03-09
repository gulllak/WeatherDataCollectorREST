package ru.evgenii.WeatherDataCollectorREST.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MeasurementDTO {
    @NotNull(message = "Value should not be empty; ")
    @Min(value = -100, message = "The value must contain from -100 to 100 degrees; ")
    @Max(value = 100, message = "The value must contain from -100 to 100 degrees; ")
    private Double value;
    @NotNull(message = "Raining should not be empty; ")
    private Boolean raining;

    @NotNull(message = "Sensor should not be empty; ")
    private SensorDTO sensor;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensor() {
        return sensor;
    }

    public void setSensor(SensorDTO sensor) {
        this.sensor = sensor;
    }
}
