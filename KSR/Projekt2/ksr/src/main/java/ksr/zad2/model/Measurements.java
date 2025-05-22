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
    private float temperature_celsius;
    private float wind_kph;
    private float pressure_mb;
    private int humidity;
    private float visibility_km;
    private float uv_index;
    private float air_quality_Carbon_Monoxide;
    private float air_quality_Nitrogen_Dioxide;
    private int air_quality_gb_defra_index;
}
