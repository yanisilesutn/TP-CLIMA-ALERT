package ar.edu.utn.frba.ddsi.receptor.dto;

import java.time.LocalDateTime;

/**
 * Copia del evento que publica el emisor. Cada servicio tiene su propia copia;
 * matchean por los NOMBRES de los campos al deserializar el JSON.
 */
public record MensajeEvento(
    String titulo,
    String contenido,
    LocalDateTime fechaHora
) {}
