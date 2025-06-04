package ksr.zad2.repository;

import ksr.zad2.model.Measurements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeasurementsRepository extends JpaRepository<Measurements, Integer> {
    @Query("SELECT m FROM Measurements m WHERE m.timezone LIKE :continent%")
    List<Measurements> findByContinent(@Param("continent") String continent);
}
