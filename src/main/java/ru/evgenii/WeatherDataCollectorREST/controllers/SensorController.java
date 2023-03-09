package ru.evgenii.WeatherDataCollectorREST.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.evgenii.WeatherDataCollectorREST.dto.SensorDTO;
import ru.evgenii.WeatherDataCollectorREST.models.Sensor;
import ru.evgenii.WeatherDataCollectorREST.services.SensorService;
import ru.evgenii.WeatherDataCollectorREST.util.ErrorResponse;
import ru.evgenii.WeatherDataCollectorREST.util.SensorNotCreatedException;
import ru.evgenii.WeatherDataCollectorREST.util.SensorValidation;

import java.util.List;

@RestController
@RequestMapping("/sensors")
public class SensorController {
    private final ModelMapper modelMapper;
    private final SensorService sensorService;
    private final SensorValidation sensorValidation;

    @Autowired
    public SensorController(ModelMapper modelMapper, SensorService sensorService, SensorValidation sensorValidation) {
        this.modelMapper = modelMapper;
        this.sensorService = sensorService;
        this.sensorValidation = sensorValidation;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid SensorDTO sensorDTO,
                                                   BindingResult bindingResult){
        Sensor sensor = sensorDtoToSensor(sensorDTO);

        sensorValidation.validate(sensor, bindingResult);

        if(bindingResult.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessage.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage());
            }
            throw new SensorNotCreatedException(errorMessage.toString());
        }
        
        sensorService.register(sensor);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(SensorNotCreatedException e){
        ErrorResponse response = new ErrorResponse(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Sensor sensorDtoToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

}
