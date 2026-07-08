package ar.edu.utn.frba.ddsi.receptor.dto;

import java.time.LocalDateTime;
import java.util.List;

public record Mensaje(
        List<String> destinatarios,
        String asunto,
        String cuerpo
) {}
