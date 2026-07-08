package ar.edu.utn.frba.ddsi.emisor.dto;

import java.time.LocalDateTime;

public record AlertaClimaEvento(
        double temperatura,
        double humedad,
        LocalDateTime fechaHora,
        String mensaje
) {
}
