package ksr.zad2.repository;

import ksr.zad2.model.Measurements;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementsRepository extends JpaRepository<Measurements, Integer> {
}
