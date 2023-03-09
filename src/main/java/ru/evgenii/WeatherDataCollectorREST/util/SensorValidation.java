package ru.evgenii.WeatherDataCollectorREST.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.evgenii.WeatherDataCollectorREST.models.Sensor;
import ru.evgenii.WeatherDataCollectorREST.services.SensorService;

@Component
public class SensorValidation implements Validator {
    private final SensorService sensorService;

    @Autowired
    public SensorValidation(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Sensor sensor = (Sensor) target;

        if(sensorService.findByName(sensor.getName()).isPresent()){
            errors.rejectValue("name","Such a sensor already exists, change the name.");
        }
    }
}
