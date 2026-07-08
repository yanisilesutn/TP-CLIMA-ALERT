package ar.edu.utn.frba.ddsi.receptor.services;

import ar.edu.utn.frba.ddsi.receptor.dto.AlertaClimaEvento;
import ar.edu.utn.frba.ddsi.receptor.dto.Mensaje;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Acá va la lógica de negocio que se ejecuta al recibir un evento
 * (por ejemplo: enviar un mail, guardar en base, actualizar métricas, etc.).
 * Por ahora solo loguea el evento como demostración.
 */
@Service
public class ProcesadorEventoService {
  private static final Logger log = LoggerFactory.getLogger(ProcesadorEventoService.class);
  private final GestorDeNotificaciones gestorDeNotificaciones;

  public ProcesadorEventoService(GestorDeNotificaciones gestorDeNotificaciones) {
    this.gestorDeNotificaciones = gestorDeNotificaciones;
  }

  public void procesar(AlertaClimaEvento evento) {
    try {
      //destinatarios
      List<String> destinatarios = List.of(
              "admin@clima.com",
              "emergencias@clima.com",
              "meteorologia@clima.com"
      );

      Mensaje nuevoMensaje = getMensaje(evento, destinatarios);

      // se lo paso al gestor para que lo encole/envie
      gestorDeNotificaciones.enviar(nuevoMensaje);

    } catch (Exception e) {
      log.error("Error al armar la notificación: {}", e.getMessage());
    }
  }

  private static @NonNull Mensaje getMensaje(AlertaClimaEvento evento, List<String> destinatarios) {
    String cuerpo = String.format(
            "Se detectaron condiciones climáticas críticas:\n\n" +
                    "- Temperatura: %s°C\n" +
                    "- Humedad: %s%%\n" +
                    "- Fecha y Hora: %s\n\n" +
                    "Detalle: %s",
            evento.temperatura(), evento.humedad(), evento.fechaHora(), evento.mensaje()
    );

    // 3. Creamos el objeto Mensaje
      return new Mensaje(
              destinatarios,
              "URGENTE: Alerta Climática Crítica",
              cuerpo
      );
  }
}
