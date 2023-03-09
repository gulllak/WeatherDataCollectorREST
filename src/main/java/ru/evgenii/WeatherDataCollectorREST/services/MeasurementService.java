package ru.evgenii.WeatherDataCollectorREST.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.evgenii.WeatherDataCollectorREST.models.Measurement;
import ru.evgenii.WeatherDataCollectorREST.repositories.MeasurementRepository;
import ru.evgenii.WeatherDataCollectorREST.repositories.SensorRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementService {
    private final MeasurementRepository measurementRepository;
    private final SensorRepository sensorRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, SensorRepository sensorRepository) {
        this.measurementRepository = measurementRepository;
        this.sensorRepository = sensorRepository;
    }

    public List<Measurement> getMeasurements() {
        return measurementRepository.findAll();
    }

    @Transactional
    public void addMeasurement(Measurement measurement) {
        enrichMeasurement(measurement);
        measurementRepository.save(measurement);
    }

    public Integer getRainyDaysCount(){
        return measurementRepository.countByRainingIsFalse();
    }

    private void enrichMeasurement(Measurement measurement) {
        //вставляем Sensor в measurement
        measurement.setSensor(sensorRepository.findByName(measurement.getSensor().getName()).get());
        measurement.setMeasurementTime(LocalDateTime.now());
    }
}
