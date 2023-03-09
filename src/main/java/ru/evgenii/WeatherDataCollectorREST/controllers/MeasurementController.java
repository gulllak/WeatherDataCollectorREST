package ru.evgenii.WeatherDataCollectorREST.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.evgenii.WeatherDataCollectorREST.dto.MeasurementDTO;
import ru.evgenii.WeatherDataCollectorREST.dto.MeasurementsResponse;
import ru.evgenii.WeatherDataCollectorREST.models.Measurement;
import ru.evgenii.WeatherDataCollectorREST.services.MeasurementService;
import ru.evgenii.WeatherDataCollectorREST.util.ErrorResponse;
import ru.evgenii.WeatherDataCollectorREST.util.MeasurementNotCreatedException;
import ru.evgenii.WeatherDataCollectorREST.util.MeasurementValidation;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {
    private final MeasurementService measurementService;
    private final ModelMapper modelMapper;
    private final MeasurementValidation measurementValidation;

    @Autowired
    public MeasurementController(MeasurementService measurementService,
                                 ModelMapper modelMapper,
                                 MeasurementValidation measurementValidation) {
        this.measurementService = measurementService;
        this.modelMapper = modelMapper;
        this.measurementValidation = measurementValidation;
    }

    @GetMapping
    public MeasurementsResponse getMeasurements() {
        return new MeasurementsResponse(measurementService.getMeasurements().stream()
                .map(this::measurementToMeasurementDTO)
                .collect(Collectors.toList()));
    }

    @GetMapping("/rainyDaysCount")
    public Integer getRainyDaysCount() {
        return measurementService.getRainyDaysCount();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                                     BindingResult bindingResult) {
        Measurement measurement = measurementDtoToMeasurement(measurementDTO);

        measurementValidation.validate(measurement, bindingResult);

        if(bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors) {
                errorMessage.append(fieldError.getField())
                        .append(" - ")
                        .append(fieldError.getDefaultMessage());
            }

            throw new MeasurementNotCreatedException(errorMessage.toString());
        }

        measurementService.addMeasurement(measurement);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(MeasurementNotCreatedException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private MeasurementDTO measurementToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    private Measurement measurementDtoToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);

    }
    // для построения графика
    @GetMapping("/chart")
    public List<Double> getDoubleList(){
        return measurementService.getMeasurements().stream().map(Measurement::getValue).toList();
    }
}
