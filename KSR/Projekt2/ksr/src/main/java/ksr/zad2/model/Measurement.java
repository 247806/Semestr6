package ksr.zad2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather_data_10_atrributes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime last_updated;
    private float temperature;
    private float wind_kph;
    private float pressure_mb;
    private int humidity;
    private float visibility_km;
    private float uv_index;
    private float air_quality_Carbon_Monoxide;
    private float air_quality_Nitrogen_Dioxide;
    private int air_quality_gb_defra_index;
}
