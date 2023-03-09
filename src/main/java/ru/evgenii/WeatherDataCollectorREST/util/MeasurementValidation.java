package ru.evgenii.WeatherDataCollectorREST.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.evgenii.WeatherDataCollectorREST.models.Measurement;
import ru.evgenii.WeatherDataCollectorREST.services.SensorService;

@Component
public class MeasurementValidation implements Validator {
    private final SensorService sensorService;

    public MeasurementValidation(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Measurement.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Measurement measurement = (Measurement) target;

        if(measurement.getSensor().getName().isEmpty()) {
            errors.rejectValue("sensor","Sensor should not be empty; ");
        } else {
            if(sensorService.findByName(measurement.getSensor().getName()).isEmpty()) {
                errors.rejectValue("sensor","Such sensor not exist; ");
            }
        }
    }
}
