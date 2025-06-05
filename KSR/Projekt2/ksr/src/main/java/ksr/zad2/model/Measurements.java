package ksr.zad2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "measurements")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Measurements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime last_updated;
    private double temperature_celsius;
    private double wind_kph;
    private double pressure_mb;
    private double humidity;
    private double visibility_km;
    private double uv_index;
    private double air_quality_Carbon_Monoxide;
    private double air_quality_Nitrogen_Dioxide;
    private double air_quality_gb_defra_index;
    private String timezone;
}
