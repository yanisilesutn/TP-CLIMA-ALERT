package ar.edu.utn.frba.ddsi.emisor.models;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class Clima {
    private Double temperatura;
    private Double humedad;
    private LocalDateTime fecha;

    public Clima(Double temperatura, Double humedad) {
        this.temperatura = temperatura;
        this.humedad = humedad;
        this.fecha= LocalDateTime.now();
    }
}
