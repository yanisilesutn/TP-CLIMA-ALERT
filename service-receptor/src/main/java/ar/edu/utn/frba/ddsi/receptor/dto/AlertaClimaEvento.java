package ar.edu.utn.frba.ddsi.receptor.dto;

import java.time.LocalDateTime;

public record AlertaClimaEvento(
        double temperatura,
        double humedad,
        LocalDateTime fechaHora,
        String mensaje
) {
}
