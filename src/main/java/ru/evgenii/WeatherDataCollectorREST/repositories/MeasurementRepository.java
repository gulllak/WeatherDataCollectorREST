package ru.evgenii.WeatherDataCollectorREST.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.evgenii.WeatherDataCollectorREST.models.Measurement;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {
    Integer countByRainingIsFalse();
}
